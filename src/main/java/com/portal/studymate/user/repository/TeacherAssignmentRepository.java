package com.portal.studymate.user.repository;

import com.portal.studymate.user.model.TeacherAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment, UUID> {
   List<TeacherAssignment> findByTeacherId(UUID teacherid);

   List<TeacherAssignment> findByClassRoomId(UUID classroomid);
}
