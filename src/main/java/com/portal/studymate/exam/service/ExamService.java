package com.portal.studymate.exam.service;

import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.BadRequestException;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.exam.dto.*;
import com.portal.studymate.exam.enums.ExamStatus;
import com.portal.studymate.exam.enums.ResultStatus;
import com.portal.studymate.exam.model.*;
import com.portal.studymate.exam.repository.*;
import com.portal.studymate.grading.service.GradingService;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.subject.repository.SubjectRepository;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ExamService {
    
    private final ExamRepository examRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamMarksRepository examMarksRepository;
    private final StudentResultRepository studentResultRepository;
    private final AcademicYearRepository academicYearRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GradingService gradingService;

    public ExamService(ExamRepository examRepository, ExamScheduleRepository examScheduleRepository,
                      ExamMarksRepository examMarksRepository, StudentResultRepository studentResultRepository,
                      AcademicYearRepository academicYearRepository, SchoolClassRepository schoolClassRepository,
                      SubjectRepository subjectRepository, StudentRepository studentRepository,
                      TeacherRepository teacherRepository, ApplicationEventPublisher applicationEventPublisher,
                      GradingService gradingService) {
        this.examRepository = examRepository;
        this.examScheduleRepository = examScheduleRepository;
        this.examMarksRepository = examMarksRepository;
        this.studentResultRepository = studentResultRepository;
        this.academicYearRepository = academicYearRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.gradingService = gradingService;
    }

    public ExamResponse createExam(CreateExamRequest request) {
        log.info("createExam called - type: {}, name: {}", request.examType(), request.name());
        var school = SchoolContext.getSchool();
        
        // Validate academic year belongs to school
        var academicYear = academicYearRepository.findById(request.academicYearId())
            .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));
        
        // Check if exam type already exists for this academic year
        if (examRepository.existsBySchoolIdAndAcademicYearIdAndExamType(
                school.getId(), request.academicYearId(), request.examType())) {
            throw new ConflictException("CONFLICT", "Exam type already exists for this academic year");
        }
        
        // Validate date range
        if (request.endDate().isBefore(request.startDate())) {
            throw new BadRequestException("BAD_REQUEST", "End date cannot be before start date");
        }
        
        var exam = new Exam(school, academicYear, request.examType(), request.name(),
                           request.startDate(), request.endDate());
        exam = examRepository.save(exam);
        
        // Publish event for exam fee generation
        applicationEventPublisher.publishEvent(new ExamCreatedEvent(
            exam.getId(), school.getId(), academicYear.getId()));
        
        return mapToExamResponse(exam);
    }

    public ExamResponse scheduleExam(CreateExamScheduleRequest request) {
        log.info("scheduleExam called - examId: {}, classId: {}", request.examId(), request.classId());
        var school = SchoolContext.getSchool();
        
        var exam = examRepository.findByIdAndSchoolId(request.examId(), school.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        
        if (exam.getStatus() != ExamStatus.DRAFT) {
            throw new BadRequestException("BAD_REQUEST", "Cannot schedule exam that is not in draft status");
        }
        
        var schoolClass = schoolClassRepository.findById(request.classId())
            .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        
        var subject = subjectRepository.findById(request.subjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        
        // Validate exam date is within exam period
        if (request.examDate().isBefore(exam.getStartDate()) || 
            request.examDate().isAfter(exam.getEndDate())) {
            throw new BadRequestException("BAD_REQUEST", "Exam date must be within exam period");
        }
        
        // Validate pass marks
        if (request.passMarks() > request.maxMarks()) {
            throw new BadRequestException("BAD_REQUEST", "Pass marks cannot exceed max marks");
        }
        
        var examSchedule = new ExamSchedule(exam, schoolClass, request.section(), subject,
                                          request.examDate(), request.maxMarks(), 
                                          request.passMarks(), request.durationMinutes());
        examScheduleRepository.save(examSchedule);
        
        // Update exam status to scheduled if not already
        if (exam.getStatus() == ExamStatus.DRAFT) {
            exam.setStatus(ExamStatus.SCHEDULED);
            examRepository.save(exam);
        }
        
        return mapToExamResponse(exam);
    }

    public void enterMarks(EnterMarksRequest request, Long teacherId) {
        log.info("enterMarks called - scheduleId: {}, studentId: {}, teacherId: {}", request.examScheduleId(), request.studentId(), teacherId);
        var school = SchoolContext.getSchool();
        
        var examSchedule = examScheduleRepository.findByIdAndSchoolId(request.examScheduleId(), school.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam schedule not found"));
        
        var student = studentRepository.findById(request.studentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
        var teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        // Validate marks
        if (!Boolean.TRUE.equals(request.absent()) && request.marksObtained() == null) {
            throw new BadRequestException("BAD_REQUEST", "Marks obtained is required when student is not absent");
        }
        
        if (request.marksObtained() != null && request.marksObtained() > examSchedule.getMaxMarks()) {
            throw new BadRequestException("BAD_REQUEST", "Marks cannot exceed maximum marks");
        }
        
        var existingMarks = examMarksRepository.findByExamScheduleIdAndStudentId(
            request.examScheduleId(), request.studentId());
        
        ExamMarks examMarks;
        if (existingMarks.isPresent()) {
            examMarks = existingMarks.get();
        } else {
            examMarks = new ExamMarks(examSchedule, student, teacher);
        }
        
        examMarks.setMarksObtained(request.marksObtained());
        examMarks.setAbsent(Boolean.TRUE.equals(request.absent()));
        examMarks.setRemarks(request.remarks());
        examMarks.setEnteredBy(teacher);
        
        examMarksRepository.save(examMarks);
    }

    public void publishResults(Long examId) {
        log.info("publishResults called - examId: {}", examId);
        var school = SchoolContext.getSchool();
        
        var exam = examRepository.findByIdAndSchoolId(examId, school.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        
        if (exam.getStatus() != ExamStatus.COMPLETED && exam.getStatus() != ExamStatus.SCHEDULED) {
            throw new BadRequestException("BAD_REQUEST", "Cannot publish results for exam that is not completed or scheduled");
        }
        
        // Generate results for all students
        generateStudentResults(exam);
        
        exam.setPublishResult(true);
        exam.setStatus(ExamStatus.PUBLISHED);
        examRepository.save(exam);
    }

    private void generateStudentResults(Exam exam) {
        var school = SchoolContext.getSchool();
        var examSchedules = examScheduleRepository.findByExamId(exam.getId());
        
        // Get all students who have marks in this exam
        var allMarks = examSchedules.stream()
            .flatMap(schedule -> examMarksRepository.findByExamScheduleId(schedule.getId()).stream())
            .toList();
        
        var studentIds = allMarks.stream()
            .map(marks -> marks.getStudent().getId())
            .distinct()
            .toList();
        
        for (Long studentId : studentIds) {
            var studentMarks = allMarks.stream()
                .filter(marks -> marks.getStudent().getId().equals(studentId))
                .toList();
            
            var totalMarks = studentMarks.stream()
                .filter(marks -> !marks.getAbsent() && marks.getMarksObtained() != null)
                .mapToInt(ExamMarks::getMarksObtained)
                .sum();
            
            var maxPossibleMarks = studentMarks.stream()
                .mapToInt(marks -> marks.getExamSchedule().getMaxMarks())
                .sum();
            
            var percentage = maxPossibleMarks > 0 ? 
                BigDecimal.valueOf(totalMarks * 100.0 / maxPossibleMarks).setScale(2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;
            
            var resultStatus = studentMarks.stream()
                .allMatch(marks -> marks.getAbsent() || 
                         (marks.getMarksObtained() != null && 
                          marks.getMarksObtained() >= marks.getExamSchedule().getPassMarks())) ?
                ResultStatus.PASS : ResultStatus.FAIL;
            
            var existingResult = studentResultRepository.findByExamIdAndStudentId(exam.getId(), studentId);
            StudentResult result;
            if (existingResult.isPresent()) {
                result = existingResult.get();
            } else {
                result = new StudentResult(studentMarks.get(0).getStudent(), exam);
            }
            
            result.setTotalMarks(totalMarks);
            result.setMaxPossibleMarks(maxPossibleMarks);
            result.setPercentage(percentage);
            result.setGrade(gradingService.calculateGrade(school.getId(), percentage));
            result.setResultStatus(resultStatus);
            
            studentResultRepository.save(result);
        }
        
        // Calculate ranks
        calculateRanks(exam);
    }
    
    private void calculateRanks(Exam exam) {
        var results = studentResultRepository.findByExamIdOrderByPercentageDesc(exam.getId());
        
        for (int i = 0; i < results.size(); i++) {
            var result = results.get(i);
            result.setRankInClass(i + 1);
            studentResultRepository.save(result);
        }
    }
    

    @Transactional(readOnly = true)
    public List<ExamResponse> getAllExams() {
        log.info("getAllExams called");
        var school = SchoolContext.getSchool();
        return examRepository.findBySchoolId(school.getId())
            .stream()
            .map(this::mapToExamResponse)
            .toList();
    }

    private ExamResponse mapToExamResponse(Exam exam) {
        return new ExamResponse(
            exam.getId(),
            exam.getAcademicYear().getId(),
            exam.getAcademicYear().getYear(),
            exam.getExamType(),
            exam.getName(),
            exam.getStartDate(),
            exam.getEndDate(),
            exam.getPublishResult(),
            exam.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public List<ExamScheduleResponse> getSchedulesForExam(Long examId) {
        log.info("getSchedulesForExam called - examId: {}", examId);
        var school = SchoolContext.getSchool();
        examRepository.findByIdAndSchoolId(examId, school.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        return examScheduleRepository.findByExamId(examId).stream()
            .map(s -> new ExamScheduleResponse(
                s.getId(), s.getExam().getId(),
                s.getSchoolClass().getId(), s.getSchoolClass().getName(),
                s.getSection(),
                s.getSubject().getId(), s.getSubject().getName(),
                s.getExamDate(), s.getMaxMarks(), s.getPassMarks(), s.getDurationMinutes()
            )).toList();
    }

    @Transactional(readOnly = true)
    public List<StudentResultResponse> getClassResults(Long examId) {
        log.info("getClassResults called - examId: {}", examId);
        var school = SchoolContext.getSchool();
        examRepository.findByIdAndSchoolId(examId, school.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        return studentResultRepository.findByExamIdOrderByPercentageDesc(examId).stream()
            .map(r -> new StudentResultResponse(
                r.getStudent().getId(),
                r.getStudent().getFullName(),
                r.getStudent().getAdmissionNumber(),
                r.getTotalMarks(), r.getMaxPossibleMarks(),
                r.getPercentage(), r.getGrade(),
                r.getRankInClass(), r.getResultStatus()
            )).toList();
    }

    public void addGraceMarks(Long examId, GraceMarkRequest request) {
        log.info("addGraceMarks called - examId: {}", examId);
        var school = SchoolContext.getSchool();
        var exam = examRepository.findByIdAndSchoolId(examId, school.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new BadRequestException("BAD_REQUEST", "Cannot add grace marks to published exam");
        }

        for (var entry : request.entries()) {
            var marks = examMarksRepository.findByExamScheduleIdAndStudentId(
                entry.examScheduleId(), entry.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Marks not found for student"));

            if (marks.getAbsent()) continue;

            int newMarks = (marks.getMarksObtained() != null ? marks.getMarksObtained() : 0) + entry.additionalMarks();
            int maxMarks = marks.getExamSchedule().getMaxMarks();
            marks.setMarksObtained(Math.min(newMarks, maxMarks));
            examMarksRepository.save(marks);
        }
    }
}