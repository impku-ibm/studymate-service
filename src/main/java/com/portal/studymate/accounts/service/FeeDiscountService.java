package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.requests.CreateFeeDiscountRequest;
import com.portal.studymate.accounts.dtos.responses.FeeDiscountResponse;
import com.portal.studymate.accounts.model.FeeDiscount;
import com.portal.studymate.accounts.repository.FeeDiscountRepository;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FeeDiscountService {

    private final FeeDiscountRepository feeDiscountRepository;
    private final StudentRepository studentRepository;
    private final AcademicYearRepository academicYearRepository;

    public FeeDiscountResponse createDiscount(CreateFeeDiscountRequest request) {
        log.info("createDiscount called - studentId: {}", request.studentId());
        var school = SchoolContext.getSchool();
        var student = studentRepository.findById(request.studentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        var year = academicYearRepository.findById(request.academicYearId())
            .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

        FeeDiscount discount = FeeDiscount.builder()
            .school(school)
            .student(student)
            .academicYear(year)
            .discountType(request.discountType())
            .percentage(request.percentage())
            .fixedAmount(request.fixedAmount())
            .reason(request.reason())
            .active(true)
            .build();

        discount = feeDiscountRepository.save(discount);
        return toResponse(discount);
    }

    @Transactional(readOnly = true)
    public List<FeeDiscountResponse> getStudentDiscounts(Long studentId) {
        log.info("getStudentDiscounts called - studentId: {}", studentId);
        return feeDiscountRepository.findByStudentAndActiveTrue(
            studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"))
        ).stream().map(this::toResponse).toList();
    }

    public void deleteDiscount(Long id) {
        log.info("deleteDiscount called - id: {}", id);
        FeeDiscount d = feeDiscountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));
        d.setActive(false);
        feeDiscountRepository.save(d);
    }

    public BigDecimal calculateDiscountedAmount(BigDecimal originalAmount, Long studentId, Long academicYearId) {
        log.info("calculateDiscountedAmount called - studentId: {}, academicYearId: {}", studentId, academicYearId);
        var discounts = feeDiscountRepository
            .findByStudentIdAndAcademicYearIdAndActiveTrue(studentId, academicYearId);

        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (var d : discounts) {
            if (d.getPercentage() != null) {
                totalDiscount = totalDiscount.add(
                    originalAmount.multiply(d.getPercentage()).divide(BigDecimal.valueOf(100)));
            }
            if (d.getFixedAmount() != null) {
                totalDiscount = totalDiscount.add(d.getFixedAmount());
            }
        }

        BigDecimal result = originalAmount.subtract(totalDiscount);
        return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
    }

    private FeeDiscountResponse toResponse(FeeDiscount d) {
        return new FeeDiscountResponse(d.getId(), d.getStudent().getId(),
            d.getStudent().getFullName(), d.getDiscountType(),
            d.getPercentage(), d.getFixedAmount(), d.getReason(), d.isActive());
    }
}
