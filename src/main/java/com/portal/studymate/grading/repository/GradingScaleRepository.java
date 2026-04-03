package com.portal.studymate.grading.repository;

import com.portal.studymate.grading.model.GradingScale;
import com.portal.studymate.school.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GradingScaleRepository extends JpaRepository<GradingScale, Long> {
    List<GradingScale> findBySchool(School school);
    Optional<GradingScale> findBySchoolAndIsDefaultTrue(School school);
}
