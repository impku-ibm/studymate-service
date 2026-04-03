package com.portal.studymate.student.service;

import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.student.dto.GenerateTCRequest;
import com.portal.studymate.student.dto.TransferCertificateResponse;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.model.StudentStatus;
import com.portal.studymate.student.model.TransferCertificate;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.student.repository.TransferCertificateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransferCertificateService {

    private final TransferCertificateRepository tcRepository;
    private final StudentRepository studentRepository;

    public TransferCertificateResponse generateTC(Long studentId, GenerateTCRequest request) {
        School school = SchoolContext.getSchool();
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Check if TC already exists
        if (tcRepository.findByStudentId(studentId).isPresent()) {
            throw new RuntimeException("Transfer certificate already generated for this student");
        }

        // Generate TC number: TC-SCHOOLCODE-YEAR-SEQUENCE
        String tcNumber = "TC-" + school.getSchoolCode() + "-" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        TransferCertificate tc = TransferCertificate.builder()
            .school(school)
            .student(student)
            .tcNumber(tcNumber)
            .leavingDate(request.getLeavingDate())
            .reasonForLeaving(request.getReasonForLeaving())
            .conduct(request.getConduct() != null ? request.getConduct() : "GOOD")
            .remarks(request.getRemarks())
            .build();

        tc = tcRepository.save(tc);

        // Update student status
        student.setStatus(StudentStatus.TRANSFERRED);
        studentRepository.save(student);

        log.info("Generated TC {} for student {}", tcNumber, student.getFullName());

        return toResponse(tc, student);
    }

    @Transactional(readOnly = true)
    public TransferCertificateResponse getTC(Long studentId) {
        TransferCertificate tc = tcRepository.findByStudentId(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Transfer certificate not found"));
        Student student = tc.getStudent();
        return toResponse(tc, student);
    }

    private TransferCertificateResponse toResponse(TransferCertificate tc, Student student) {
        return TransferCertificateResponse.builder()
            .id(tc.getId())
            .tcNumber(tc.getTcNumber())
            .studentName(student.getFullName())
            .admissionNumber(student.getAdmissionNumber())
            .leavingDate(tc.getLeavingDate())
            .reasonForLeaving(tc.getReasonForLeaving())
            .conduct(tc.getConduct())
            .remarks(tc.getRemarks())
            .build();
    }
}
