package com.portal.studymate.teacherassignment.model;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.classmanagement.model.ClassSectionTemplate;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.model.Subject;
import com.portal.studymate.teachermgmt.model.Teacher;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
   name = "teacher_assignment",
   uniqueConstraints = {
      @UniqueConstraint(
         columnNames = {
            "academic_year_id",
            "section_id",
            "subject_id"
         }
      )
   }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAssignment {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   // Tenant safety
   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "school_id")
   private School school;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "academic_year_id")
   private AcademicYear academicYear;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "section_id")
   private ClassSectionTemplate section;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "subject_id")
   private Subject subject;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "teacher_id")
   private Teacher teacher;

   private boolean active = true;
}
