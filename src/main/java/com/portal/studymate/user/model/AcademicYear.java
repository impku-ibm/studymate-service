package com.portal.studymate.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "academic_years")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYear extends BaseEntity{
   @Column(name = "school_id", nullable = false)
   private UUID schoolId;

   @Column(name = "year_label", nullable = false)
   private String yearLabel;

   private boolean active = true;
}
