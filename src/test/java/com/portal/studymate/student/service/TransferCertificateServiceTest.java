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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferCertificateServiceTest {

    @Mock private TransferCertificateRepository tcRepository;
    @Mock private StudentRepository studentRepository;

    @InjectMocks private TransferCertificateService transferCertificateService;

    @Test
    void generateTC_success() {
        School school = new School();
        school.setId(1L);
        school.setSchoolCode("SCH001");

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            Student student = Student.builder()
                    .id(1L).fullName("Test Student").admissionNumber("ADM001")
                    .status(StudentStatus.ACTIVE).build();
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(tcRepository.findByStudentId(1L)).thenReturn(Optional.empty());
            when(tcRepository.save(any(TransferCertificate.class))).thenAnswer(inv -> {
                TransferCertificate tc = inv.getArgument(0);
                tc.setId(1L);
                return tc;
            });
            when(studentRepository.save(any(Student.class))).thenReturn(student);

            GenerateTCRequest request = new GenerateTCRequest();
            request.setLeavingDate(LocalDate.of(2024, 12, 31));
            request.setReasonForLeaving("Transfer");
            request.setConduct("GOOD");

            TransferCertificateResponse result = transferCertificateService.generateTC(1L, request);

            assertNotNull(result);
            assertEquals("Test Student", result.getStudentName());
            verify(studentRepository).save(any(Student.class));
        }
    }

    @Test
    void generateTC_alreadyExists_throws() {
        School school = new School();
        school.setId(1L);
        school.setSchoolCode("SCH001");

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            Student student = Student.builder().id(1L).fullName("Test Student").build();
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

            TransferCertificate existingTc = TransferCertificate.builder()
                    .id(1L).tcNumber("TC-001").student(student).build();
            when(tcRepository.findByStudentId(1L)).thenReturn(Optional.of(existingTc));

            GenerateTCRequest request = new GenerateTCRequest();
            request.setLeavingDate(LocalDate.of(2024, 12, 31));

            assertThrows(RuntimeException.class,
                    () -> transferCertificateService.generateTC(1L, request));
        }
    }

    @Test
    void getTC_success() {
        Student student = Student.builder()
                .id(1L).fullName("Test Student").admissionNumber("ADM001").build();
        TransferCertificate tc = TransferCertificate.builder()
                .id(1L).tcNumber("TC-SCH001-20241231")
                .student(student).leavingDate(LocalDate.of(2024, 12, 31))
                .reasonForLeaving("Transfer").conduct("GOOD").remarks("None").build();

        when(tcRepository.findByStudentId(1L)).thenReturn(Optional.of(tc));

        TransferCertificateResponse result = transferCertificateService.getTC(1L);

        assertNotNull(result);
        assertEquals("TC-SCH001-20241231", result.getTcNumber());
        assertEquals("Test Student", result.getStudentName());
        assertEquals("ADM001", result.getAdmissionNumber());
    }

    @Test
    void getTC_notFound_throws() {
        when(tcRepository.findByStudentId(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> transferCertificateService.getTC(99L));
    }
}
