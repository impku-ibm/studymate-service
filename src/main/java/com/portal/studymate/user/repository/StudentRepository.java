package com.portal.studymate.user.repository;

import com.portal.studymate.user.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
   List<Student> findByClassRoomId(UUID classroomid);

   List<Student> findBySchoolId(UUID schoolid);
}
