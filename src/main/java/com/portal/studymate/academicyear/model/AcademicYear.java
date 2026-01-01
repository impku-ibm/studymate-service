package com.portal.studymate.academicyear.model;

import com.portal.studymate.school.model.School;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
   name = "academic_year",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"school_id", "year"})
   }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYear {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "school_id")
   private School school;

   @Column(nullable = false)
   private String year; // 2024-25

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private AcademicYearStatus status; // ACTIVE / COMPLETED

   private boolean active = true;

   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();
}

