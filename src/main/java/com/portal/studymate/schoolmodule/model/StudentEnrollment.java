package com.portal.studymate.schoolmodule.model;

import com.portal.studymate.schoolmodule.utils.EnrollmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
   name = "student_enrollments",
   uniqueConstraints = @UniqueConstraint(
      columnNames = {"student_id", "academic_year_id"}
   )
)
@Data
public class StudentEnrollment {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String schoolId;

   @ManyToOne(fetch = FetchType.LAZY)
   private Student student;

   @ManyToOne(fetch = FetchType.LAZY)
   private AcademicYearEntity academicYear;

   @ManyToOne(fetch = FetchType.LAZY)
   private ClassEntity schoolClass;

   @ManyToOne(fetch = FetchType.LAZY)
   private SectionEntity section;

   @Column(nullable = false)
   private Integer rollNumber;

   @Enumerated(EnumType.STRING)
   private EnrollmentStatus status;

   @CreationTimestamp
   private LocalDateTime createdAt;
}
