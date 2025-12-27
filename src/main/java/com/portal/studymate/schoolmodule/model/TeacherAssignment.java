package com.portal.studymate.schoolmodule.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
   name = "teacher_assignments",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {
         "teacher_id", "class_id", "section", "subject_id", "academic_year_id"
      })
   }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAssignment {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String schoolId;

   @ManyToOne(fetch = FetchType.LAZY)
   private Teacher teacher;

   private Long classId;
   private String section;
   private Long subjectId;
   private Long academicYearId;

   private LocalDateTime createdAt;

   @PrePersist
   void onCreate() {
      createdAt = LocalDateTime.now();
   }
}

