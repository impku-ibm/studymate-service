package com.portal.studymate.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
   name = "teacher_assignments",
   uniqueConstraints = @UniqueConstraint(
      columnNames = {"teacher_id", "class_room_id", "subject_id"}
   )
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAssignment extends BaseEntity {

   @Column(name = "teacher_id", nullable = false)
   private UUID teacherId;

   @Column(name = "class_room_id", nullable = false)
   private UUID classRoomId;

   @Column(name = "subject_id", nullable = false)
   private UUID subjectId;
}

