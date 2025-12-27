package com.portal.studymate.schoolmodule.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
   name = "class_subject",
   uniqueConstraints = @UniqueConstraint(
      columnNames = {
         "academic_year_id",
         "class_id",
         "section_id",
         "subject_id"
      }
   )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSubject {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String schoolId;

   @Column(nullable = false)
   private Long academicYearId;

   @Column(nullable = false)
   private Long classId;

   private Long sectionId;

   @Column(nullable = false)
   private Long subjectId;

   private Integer weeklyPeriods;

   @Column(name = "is_optional", nullable = false)
   private boolean optional;
}

