package com.portal.studymate.user.repository;

import com.portal.studymate.user.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
   List<Teacher> findBySchoolId(UUID schoolid);
}
