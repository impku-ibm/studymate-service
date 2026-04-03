package com.portal.studymate.timetable.repository;

import com.portal.studymate.timetable.model.TimetableEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, Long> {
    List<TimetableEntry> findBySchoolClassIdAndSectionOrderByDayOfWeekAscPeriodDefinitionPeriodNumberAsc(Long classId, String section);
    List<TimetableEntry> findByTeacherIdOrderByDayOfWeekAscPeriodDefinitionPeriodNumberAsc(Long teacherId);
    List<TimetableEntry> findByTeacherIdAndDayOfWeekAndPeriodDefinitionId(Long teacherId, Integer dayOfWeek, Long periodId);
    List<TimetableEntry> findBySchoolClassIdAndSectionAndDayOfWeekAndPeriodDefinitionId(Long classId, String section, Integer dayOfWeek, Long periodId);
}
