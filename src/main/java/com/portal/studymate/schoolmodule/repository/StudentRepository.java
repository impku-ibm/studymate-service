package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.schoolmodule.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
   long countBySchoolId(String schoolId);
   boolean existsBySchoolIdAndFullNameAndDateOfBirthAndParentMobile(
      String schoolId,
      String fullName,
      LocalDate dateOfBirth,
      String parentMobile
   );

   List<Student> findBySchoolIdOrderByCreatedAtDesc(String schoolId);

   Optional<Student> findByIdAndSchoolId(Long id, String schoolId);
}
