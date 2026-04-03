package com.portal.studymate.staff.repository;

import com.portal.studymate.staff.model.StaffAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StaffAttendanceRepository extends JpaRepository<StaffAttendance, Long> {
    Optional<StaffAttendance> findByStaffIdAndAttendanceDate(Long staffId, LocalDate date);
    List<StaffAttendance> findByStaffIdAndAttendanceDateBetween(Long staffId, LocalDate start, LocalDate end);
}
