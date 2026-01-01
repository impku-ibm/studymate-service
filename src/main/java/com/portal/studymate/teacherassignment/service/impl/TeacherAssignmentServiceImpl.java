package com.portal.studymate.teacherassignment.service.impl;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.model.ClassSectionTemplate;
import com.portal.studymate.classmanagement.repository.ClassSectionTemplateRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.model.Subject;
import com.portal.studymate.subject.repository.SubjectRepository;
import com.portal.studymate.teacherassignment.dto.CreateTeacherAssignmentRequest;
import com.portal.studymate.teacherassignment.dto.TeacherAssignmentResponse;
import com.portal.studymate.teacherassignment.model.TeacherAssignment;
import com.portal.studymate.teacherassignment.repository.TeacherAssignmentRepository;
import com.portal.studymate.teacherassignment.service.TeacherAssignmentService;
import com.portal.studymate.teachermgmt.model.Teacher;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherAssignmentServiceImpl
   implements TeacherAssignmentService {

   private final TeacherAssignmentRepository assignmentRepository;
   private final TeacherRepository teacherRepository;
   private final SubjectRepository subjectRepository;
   private final ClassSectionTemplateRepository sectionRepository;
   private final AcademicYearRepository academicYearRepository;

   @Override
   public TeacherAssignmentResponse assignTeacher(
      CreateTeacherAssignmentRequest req
   ) {

      School school = SchoolContext.getSchool();

      AcademicYear activeYear =
         academicYearRepository
            .findBySchoolAndStatus(
               school, AcademicYearStatus.ACTIVE)
            .orElseThrow(() ->
                            new ResourceNotFoundException(
                               "ACTIVE_ACADEMIC_YEAR_NOT_FOUND",
                               "No active academic year"
                            )
            );

      Teacher teacher = teacherRepository.findById(req.getTeacherId())
                                         .orElseThrow(() ->
                                                         new ResourceNotFoundException(
                                                            "TEACHER_NOT_FOUND",
                                                            "Teacher not found"
                                                         )
                                         );

      Subject subject = subjectRepository.findById(req.getSubjectId())
                                         .orElseThrow(() ->
                                                         new ResourceNotFoundException(
                                                            "SUBJECT_NOT_FOUND",
                                                            "Subject not found"
                                                         )
                                         );

      ClassSectionTemplate section =
         sectionRepository.findById(req.getSectionId())
                          .orElseThrow(() ->
                                          new ResourceNotFoundException(
                                             "SECTION_NOT_FOUND",
                                             "Section not found"
                                          )
                          );

      assignmentRepository
         .findByAcademicYearAndSectionIdAndSubjectId(
            activeYear,
            section.getId(),
            subject.getId()
         )
         .ifPresent(a -> {
            throw new ConflictException(
               "ASSIGNMENT_EXISTS",
               "Teacher already assigned for this subject & section"
            );
         });

      TeacherAssignment assignment = TeacherAssignment.builder()
                                                      .school(school)
                                                      .academicYear(activeYear)
                                                      .teacher(teacher)
                                                      .subject(subject)
                                                      .section(section)
                                                      .active(true)
                                                      .build();

      return toResponse(
         assignmentRepository.save(assignment)
      );
   }

   @Override
   @Transactional(readOnly = true)
   public List<TeacherAssignmentResponse>
   getAssignmentsForSection(Long sectionId) {

      School school = SchoolContext.getSchool();

      AcademicYear activeYear =
         academicYearRepository
            .findBySchoolAndStatus(
               school, AcademicYearStatus.ACTIVE)
            .orElseThrow(() ->
                            new ResourceNotFoundException(
                               "ACTIVE_ACADEMIC_YEAR_NOT_FOUND",
                               "No active academic year"
                            )
            );

      return assignmentRepository
                .findByAcademicYearAndSectionId(
                   activeYear, sectionId
                )
                .stream()
                .map(this::toResponse)
                .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public List<TeacherAssignmentResponse>
   getAssignmentsForTeacher() {

      String userId =
         (String) org.springframework.security
                     .core.context.SecurityContextHolder
                     .getContext()
                     .getAuthentication()
                     .getPrincipal();

      Teacher teacher =
         teacherRepository.findByUserId(userId)
                          .orElseThrow(() ->
                                          new ResourceNotFoundException(
                                             "TEACHER_PROFILE_NOT_FOUND",
                                             "Teacher profile not found"
                                          )
                          );

      AcademicYear activeYear =
         academicYearRepository
            .findBySchoolAndStatus(
               teacher.getSchool(),
               AcademicYearStatus.ACTIVE)
            .orElseThrow();

      return assignmentRepository
                .findByTeacherAndAcademicYear(
                   teacher, activeYear
                )
                .stream()
                .map(this::toResponse)
                .toList();
   }

   private TeacherAssignmentResponse toResponse(
      TeacherAssignment a
   ) {
      return TeacherAssignmentResponse.builder()
                                      .id(a.getId())
                                      .teacherId(a.getTeacher().getId())
                                      .teacherName(a.getTeacher().getFullName())
                                      .subjectId(a.getSubject().getId())
                                      .subjectName(a.getSubject().getName())
                                      .sectionId(a.getSection().getId())
                                      .sectionName(a.getSection().getName())
                                      .active(a.isActive())
                                      .build();
   }
}

