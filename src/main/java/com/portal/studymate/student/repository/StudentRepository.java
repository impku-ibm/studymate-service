package com.portal.studymate.student.repository;

import com.portal.studymate.school.model.School;
import com.portal.studymate.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

   List<Student> findBySchool(School school);

   Optional<Student> findByUserId(String userId);

   long countBySchool(School school);
}