package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.requests.CreateFeeStructureRequest;
import com.portal.studymate.accounts.dtos.responses.FeeStructureResponse;
import com.portal.studymate.accounts.enums.FeeType;
import com.portal.studymate.accounts.model.FeeStructure;
import com.portal.studymate.accounts.repository.FeeStructureRepository;
import com.portal.studymate.accounts.service.impl.FeeStructureServiceImpl;
import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeStructureServiceTest {

    @Mock private FeeStructureRepository feeStructureRepository;
    @Mock private AcademicYearRepository academicYearRepository;
    @Mock private SchoolClassRepository schoolClassRepository;
    @Mock private AuditService auditService;

    @InjectMocks private FeeStructureServiceImpl feeStructureService;

    private School school;
    private AcademicYear academicYear;
    private SchoolClass schoolClass;

    @BeforeEach
    void setUp() {
        school = new School();
        school.setId(1L);
        academicYear = AcademicYear.builder().id(1L).year("2024-2025").build();
        schoolClass = SchoolClass.builder().id(1L).name("Class 10").build();
    }

    @Test
    void createFeeStructure_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(academicYearRepository.findById(1L)).thenReturn(Optional.of(academicYear));
            when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));
            when(feeStructureRepository.findBySchoolAndAcademicYearAndSchoolClassAndFeeType(
                any(), any(), any(), any())).thenReturn(Optional.empty());
            when(feeStructureRepository.save(any(FeeStructure.class))).thenAnswer(inv -> {
                FeeStructure fs = inv.getArgument(0);
                fs.setId(1L);
                return fs;
            });

            var request = new CreateFeeStructureRequest(1L, 1L, FeeType.TUITION,
                BigDecimal.valueOf(5000), LocalDate.of(2024, 7, 10));

            FeeStructureResponse result = feeStructureService.createFeeStructure(request);
            assertNotNull(result);
            assertEquals(FeeType.TUITION, result.feeType());
            assertEquals(0, BigDecimal.valueOf(5000).compareTo(result.amount()));
        }
    }

    @Test
    void deleteFeeStructure_success() {
        FeeStructure fs = new FeeStructure();
        fs.setId(1L);
        when(feeStructureRepository.findById(1L)).thenReturn(Optional.of(fs));

        feeStructureService.deleteFeeStructure(1L);
        verify(feeStructureRepository).delete(fs);
    }

    @Test
    void deleteFeeStructure_notFound_throws() {
        when(feeStructureRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> feeStructureService.deleteFeeStructure(99L));
    }

    @Test
    void toggleFeeStructure_togglesActive() {
        FeeStructure fs = new FeeStructure();
        fs.setId(1L);
        fs.setActive(true);
        fs.setAcademicYear(academicYear);
        fs.setSchoolClass(schoolClass);
        fs.setFeeType(FeeType.TUITION);
        fs.setAmount(BigDecimal.valueOf(5000));
        fs.setDueDate(LocalDate.of(2024, 7, 10));

        when(feeStructureRepository.findById(1L)).thenReturn(Optional.of(fs));
        when(feeStructureRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FeeStructureResponse result = feeStructureService.toggleFeeStructure(1L);
        assertFalse(result.active());
    }
}
