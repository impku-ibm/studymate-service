package com.portal.studymate.staff.repository;

import com.portal.studymate.school.model.School;
import com.portal.studymate.staff.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findBySchool(School school);
    List<Staff> findBySchoolAndActiveTrue(School school);
    Optional<Staff> findByUserId(String userId);
}
