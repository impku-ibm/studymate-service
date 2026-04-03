package com.portal.studymate.grading.service;

import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.grading.dto.CreateGradingScaleRequest;
import com.portal.studymate.grading.dto.GradingScaleResponse;
import com.portal.studymate.grading.model.GradingScale;
import com.portal.studymate.grading.model.GradingScaleEntry;
import com.portal.studymate.grading.repository.GradingScaleRepository;
import com.portal.studymate.school.model.School;
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
public class GradingService {

    private final GradingScaleRepository gradingScaleRepository;

    public GradingScaleResponse createGradingScale(CreateGradingScaleRequest request) {
        log.info("createGradingScale called - name: {}", request.name());
        School school = SchoolContext.getSchool();

        GradingScale scale = GradingScale.builder()
            .school(school)
            .name(request.name())
            .isDefault(request.isDefault())
            .active(true)
            .build();

        List<GradingScaleEntry> entries = request.entries().stream()
            .map(e -> GradingScaleEntry.builder()
                .gradingScale(scale)
                .gradeName(e.gradeName())
                .minPercentage(e.minPercentage())
                .maxPercentage(e.maxPercentage())
                .gradePoint(e.gradePoint())
                .description(e.description())
                .build())
            .toList();

        scale.setEntries(entries);

        // If this is default, unset other defaults
        if (request.isDefault()) {
            gradingScaleRepository.findBySchoolAndIsDefaultTrue(school)
                .ifPresent(existing -> {
                    existing.setDefault(false);
                    gradingScaleRepository.save(existing);
                });
        }

        GradingScale saved = gradingScaleRepository.save(scale);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<GradingScaleResponse> getGradingScales() {
        log.info("getGradingScales called");
        School school = SchoolContext.getSchool();
        return gradingScaleRepository.findBySchool(school).stream()
            .map(this::toResponse).toList();
    }

    public void deleteGradingScale(Long id) {
        log.info("deleteGradingScale called - id: {}", id);
        gradingScaleRepository.deleteById(id);
    }

    /**
     * Calculate grade using the school's default grading scale.
     * Falls back to hardcoded CBSE grades if no scale is configured.
     */
    public String calculateGrade(Long schoolId, BigDecimal percentage) {
        log.info("calculateGrade called - schoolId: {}, percentage: {}", schoolId, percentage);
        School school = SchoolContext.getSchool();
        var scale = gradingScaleRepository.findBySchoolAndIsDefaultTrue(school);

        if (scale.isPresent() && scale.get().getEntries() != null) {
            for (var entry : scale.get().getEntries()) {
                if (percentage.compareTo(entry.getMinPercentage()) >= 0 &&
                    percentage.compareTo(entry.getMaxPercentage()) <= 0) {
                    return entry.getGradeName();
                }
            }
        }

        // Fallback: CBSE default
        if (percentage.compareTo(BigDecimal.valueOf(91)) >= 0) return "A1";
        if (percentage.compareTo(BigDecimal.valueOf(81)) >= 0) return "A2";
        if (percentage.compareTo(BigDecimal.valueOf(71)) >= 0) return "B1";
        if (percentage.compareTo(BigDecimal.valueOf(61)) >= 0) return "B2";
        if (percentage.compareTo(BigDecimal.valueOf(51)) >= 0) return "C1";
        if (percentage.compareTo(BigDecimal.valueOf(41)) >= 0) return "C2";
        if (percentage.compareTo(BigDecimal.valueOf(33)) >= 0) return "D";
        return "E";
    }

    private GradingScaleResponse toResponse(GradingScale scale) {
        var entries = scale.getEntries() == null ? List.<GradingScaleResponse.GradingEntryResponse>of() :
            scale.getEntries().stream()
                .map(e -> new GradingScaleResponse.GradingEntryResponse(
                    e.getId(), e.getGradeName(), e.getMinPercentage(),
                    e.getMaxPercentage(), e.getGradePoint(), e.getDescription()))
                .toList();
        return new GradingScaleResponse(scale.getId(), scale.getName(),
            scale.isDefault(), scale.isActive(), entries);
    }
}
