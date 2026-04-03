package com.portal.studymate.accounts.repository;

import com.portal.studymate.accounts.model.FeePlan;
import com.portal.studymate.school.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeePlanRepository extends JpaRepository<FeePlan, Long> {
    List<FeePlan> findBySchoolAndActiveTrue(School school);
    List<FeePlan> findBySchool(School school);
}
