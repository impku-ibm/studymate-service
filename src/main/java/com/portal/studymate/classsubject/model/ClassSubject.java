package com.portal.studymate.classsubject.model;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.subject.model.Subject;
import jakarta.persistence.Entity;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
   name = "class_subject",
   uniqueConstraints = {
      @UniqueConstraint(
         columnNames = {"academic_year_id", "school_class_id", "subject_id"}
      )
   }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSubject {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "academic_year_id")
   private AcademicYear academicYear;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "school_class_id")
   private SchoolClass schoolClass;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "subject_id")
   private Subject subject;

   private boolean active = true;
}

