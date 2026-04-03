package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.enums.DiscountType;
import com.portal.studymate.accounts.model.FeeDiscount;
import com.portal.studymate.accounts.repository.FeeDiscountRepository;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.student.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeDiscountServiceTest {

    @Mock private FeeDiscountRepository feeDiscountRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private AcademicYearRepository academicYearRepository;

    @InjectMocks private FeeDiscountService feeDiscountService;

    @Test
    void calculateDiscountedAmount_withPercentageDiscount() {
        FeeDiscount discount = FeeDiscount.builder()
            .discountType(DiscountType.SIBLING)
            .percentage(BigDecimal.valueOf(10))
            .active(true)
            .build();

        when(feeDiscountRepository.findByStudentIdAndAcademicYearIdAndActiveTrue(1L, 1L))
            .thenReturn(List.of(discount));

        BigDecimal result = feeDiscountService.calculateDiscountedAmount(
            BigDecimal.valueOf(5000), 1L, 1L);

        assertEquals(0, BigDecimal.valueOf(4500).compareTo(result));
    }

    @Test
    void calculateDiscountedAmount_withFixedDiscount() {
        FeeDiscount discount = FeeDiscount.builder()
            .discountType(DiscountType.RTE_QUOTA)
            .fixedAmount(BigDecimal.valueOf(2000))
            .active(true)
            .build();

        when(feeDiscountRepository.findByStudentIdAndAcademicYearIdAndActiveTrue(1L, 1L))
            .thenReturn(List.of(discount));

        BigDecimal result = feeDiscountService.calculateDiscountedAmount(
            BigDecimal.valueOf(5000), 1L, 1L);

        assertEquals(0, BigDecimal.valueOf(3000).compareTo(result));
    }

    @Test
    void calculateDiscountedAmount_multipleDiscounts_additive() {
        FeeDiscount d1 = FeeDiscount.builder()
            .discountType(DiscountType.SIBLING).percentage(BigDecimal.valueOf(10)).active(true).build();
        FeeDiscount d2 = FeeDiscount.builder()
            .discountType(DiscountType.MERIT_SCHOLARSHIP).fixedAmount(BigDecimal.valueOf(500)).active(true).build();

        when(feeDiscountRepository.findByStudentIdAndAcademicYearIdAndActiveTrue(1L, 1L))
            .thenReturn(List.of(d1, d2));

        // 5000 - 10% (500) - 500 fixed = 4000
        BigDecimal result = feeDiscountService.calculateDiscountedAmount(
            BigDecimal.valueOf(5000), 1L, 1L);

        assertEquals(0, BigDecimal.valueOf(4000).compareTo(result));
    }

    @Test
    void calculateDiscountedAmount_cappedAtZero() {
        FeeDiscount discount = FeeDiscount.builder()
            .discountType(DiscountType.RTE_QUOTA)
            .fixedAmount(BigDecimal.valueOf(10000))
            .active(true)
            .build();

        when(feeDiscountRepository.findByStudentIdAndAcademicYearIdAndActiveTrue(1L, 1L))
            .thenReturn(List.of(discount));

        BigDecimal result = feeDiscountService.calculateDiscountedAmount(
            BigDecimal.valueOf(5000), 1L, 1L);

        assertEquals(0, BigDecimal.ZERO.compareTo(result));
    }

    @Test
    void calculateDiscountedAmount_noDiscounts_returnsOriginal() {
        when(feeDiscountRepository.findByStudentIdAndAcademicYearIdAndActiveTrue(1L, 1L))
            .thenReturn(List.of());

        BigDecimal result = feeDiscountService.calculateDiscountedAmount(
            BigDecimal.valueOf(5000), 1L, 1L);

        assertEquals(0, BigDecimal.valueOf(5000).compareTo(result));
    }
}
