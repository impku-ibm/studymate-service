package com.portal.studymate.student.model;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.classmanagement.model.ClassSectionTemplate;
import com.portal.studymate.classmanagement.model.SchoolClass;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
   name = "student_enrollment",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"student_id", "academic_year_id"}),
      @UniqueConstraint(columnNames = {"academic_year_id", "section_id", "roll_number"})
   }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollment {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   private Student student;

   @ManyToOne(fetch = FetchType.LAZY)
   private AcademicYear academicYear;

   @ManyToOne(fetch = FetchType.LAZY)
   private SchoolClass schoolClass;

   @ManyToOne(fetch = FetchType.LAZY)
   private ClassSectionTemplate section;

   private Integer rollNumber;

   @Enumerated(EnumType.STRING)
   private EnrollmentStatus status;
}

