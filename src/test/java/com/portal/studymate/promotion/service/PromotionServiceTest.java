package com.portal.studymate.promotion.service;

import com.portal.studymate.promotion.dto.BulkPromotionRequest;
import com.portal.studymate.promotion.dto.PromotionResultResponse;
import com.portal.studymate.student.dto.StudentEnrollmentResponse;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.student.service.StudentEnrollmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    @Mock private StudentEnrollmentService enrollmentService;
    @Mock private StudentRepository studentRepository;

    @InjectMocks private PromotionService promotionService;

    @Test
    void bulkPromote_success() {
        Student student = Student.builder().id(1L).fullName("Student A").build();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentService.enroll(any())).thenReturn(mock(StudentEnrollmentResponse.class));

        var request = new BulkPromotionRequest(1L, "A", 2L, "A", 1L, List.of(1L));

        PromotionResultResponse result = promotionService.bulkPromote(request);

        assertTrue(result.promotedCount() > 0);
        assertEquals(0, result.skippedCount());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    void bulkPromote_studentNotFound_addsError() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        var request = new BulkPromotionRequest(1L, "A", 2L, "A", 1L, List.of(99L));

        PromotionResultResponse result = promotionService.bulkPromote(request);

        assertTrue(result.skippedCount() > 0);
        assertFalse(result.errors().isEmpty());
    }

    @Test
    void bulkPromote_partialFailure_reportsCorrectly() {
        Student s1 = Student.builder().id(1L).fullName("Student 1").build();
        Student s2 = Student.builder().id(2L).fullName("Student 2").build();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(studentRepository.findById(2L)).thenReturn(Optional.of(s2));
        when(studentRepository.findById(3L)).thenReturn(Optional.empty());

        when(enrollmentService.enroll(any())).thenReturn(mock(StudentEnrollmentResponse.class));

        var request = new BulkPromotionRequest(1L, "A", 2L, "A", 1L, List.of(1L, 2L, 3L));

        PromotionResultResponse result = promotionService.bulkPromote(request);

        assertEquals(2, result.promotedCount());
        assertEquals(1, result.skippedCount());
        assertEquals(1, result.errors().size());
    }
}
