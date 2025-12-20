package com.portal.studymate.user.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
   name = "students",
   uniqueConstraints = @UniqueConstraint(
      columnNames = {"academic_year_id", "class_room_id", "roll_number"}
   )
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity {

   @Column(name = "school_id", nullable = false)
   private UUID schoolId;

   @Column(name = "academic_year_id", nullable = false)
   private UUID academicYearId;

   @Column(name = "class_room_id", nullable = false)
   private UUID classRoomId;

   @Column(name = "roll_number")
   private Integer rollNumber;

   @Column(name = "admission_number")
   private String admissionNumber;

   @Column(nullable = false)
   private String name;

   private LocalDate dob;

   private String gender;

   private boolean active = true;
}

