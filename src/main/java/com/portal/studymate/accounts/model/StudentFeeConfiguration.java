package com.portal.studymate.accounts.model;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.accounts.enums.FeeType;
import com.portal.studymate.student.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity to track which fee types are applicable for each student.
 * This allows flexible fee assignment - e.g., some students may not need transport or hostel fees.
 * 
 * Key Features:
 * - Configurable per student (not all students have same fees)
 * - Can be activated/deactivated (e.g., student opts out of transport mid-year)
 * - Tracks start and end dates for fee applicability
 * 
 * Example: Student A has TUITION + TRANSPORT, Student B has TUITION + HOSTEL + TRANSPORT
 */
@Entity
@Table(
   name = "student_fee_configuration",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"student_id", "academic_year_id", "fee_type"})
   }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFeeConfiguration {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   // The student for whom this fee configuration applies
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "student_id", nullable = false)
   private Student student;

   // Academic year for this fee configuration
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "academic_year_id", nullable = false)
   private AcademicYear academicYear;

   // Type of fee (TUITION, TRANSPORT, HOSTEL, EXAM, MISC)
   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private FeeType feeType;

   // Whether this fee is currently active for the student
   // Can be set to false if student opts out (e.g., stops using transport)
   @Column(nullable = false)
   private boolean active = true;

   // When this fee configuration started (usually admission date or when opted in)
   @Column(nullable = false)
   private LocalDate startDate;

   // When this fee configuration ended (null means ongoing)
   // Set when student opts out of a fee type
   private LocalDate endDate;

   // Audit fields
   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @Column(name = "updated_at")
   private LocalDateTime updatedAt = LocalDateTime.now();

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
}
