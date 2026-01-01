package com.portal.studymate.teachermgmt.repository;

import com.portal.studymate.school.model.School;
import com.portal.studymate.teachermgmt.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

   List<Teacher> findBySchool(School school);

   Optional<Teacher> findBySchoolAndEmail(School school, String email);
   Optional<Teacher> findByUserId(String userId);

   long countBySchool(School school);
}