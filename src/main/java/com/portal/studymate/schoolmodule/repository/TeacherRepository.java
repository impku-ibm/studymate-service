package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.schoolmodule.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

   List<Teacher> findBySchoolIdAndDeletedFalse(String schoolId);

   Optional<Teacher> findByIdAndSchoolId(Long id, String schoolId);

   @Query("""
   SELECT COUNT(t) FROM Teacher t
   WHERE t.schoolId = :schoolId
""")
   long countTeachers(@Param("schoolId") String schoolId);
}
