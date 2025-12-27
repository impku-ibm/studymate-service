package com.portal.studymate.schoolmodule.model;

import com.portal.studymate.common.util.AcademicYearStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="academic_years")
@Data
public class AcademicYearEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String schoolId;
   private String yearLabel;     // 2024-25

   @Enumerated(EnumType.STRING)
   private AcademicYearStatus status;

   private LocalDate startDate;
   private LocalDate endDate;
}

