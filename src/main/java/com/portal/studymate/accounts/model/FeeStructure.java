package com.portal.studymate.accounts.model;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.accounts.enums.FeeType;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.school.model.School;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
   name = "fee_structure",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"school_id", "academic_year_id", "class_id", "fee_type"})
   }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeStructure {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "school_id", nullable = false)
   private School school;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "academic_year_id", nullable = false)
   private AcademicYear academicYear;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "class_id", nullable = false)
   private SchoolClass schoolClass;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private FeeType feeType;

   @Column(nullable = false)
   private BigDecimal amount;

   @Column(nullable = false)
   private LocalDate dueDate;

   private boolean active = true;

   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();
}
