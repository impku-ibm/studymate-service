package com.portal.studymate.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
   name = "class_rooms",
   uniqueConstraints = @UniqueConstraint(
      columnNames = {"academic_year_id", "class_name", "section"}
   )
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRoom extends BaseEntity {

   @Column(name = "school_id", nullable = false)
   private UUID schoolId;

   @Column(name = "academic_year_id", nullable = false)
   private UUID academicYearId;

   @Column(name = "class_name", nullable = false)
   private String className;

   @Column(nullable = false)
   private String section;
}
