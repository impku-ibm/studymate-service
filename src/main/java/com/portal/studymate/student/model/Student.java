package com.portal.studymate.student.model;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
   name = "student",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"school_id", "admission_number"})
   }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, unique = true)
   private String userId; // Auth service user id

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "school_id", nullable = false)
   private School school;

   @Column(nullable = false)
   private String admissionNumber;

   @Column(nullable = false)
   private String fullName;

   private LocalDate dateOfBirth;
   private LocalDate admissionDate;

   private String parentName;
   private String parentMobile;
   private String address;

   @Enumerated(EnumType.STRING)
   private StudentStatus status;
}

