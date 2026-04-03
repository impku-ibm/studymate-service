package com.portal.studymate.timetable.repository;

import com.portal.studymate.school.model.School;
import com.portal.studymate.timetable.model.PeriodDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PeriodDefinitionRepository extends JpaRepository<PeriodDefinition, Long> {
    List<PeriodDefinition> findBySchoolOrderByPeriodNumber(School school);
}
