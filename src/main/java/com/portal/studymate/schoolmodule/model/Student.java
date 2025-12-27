package com.portal.studymate.schoolmodule.model;

import com.portal.studymate.schoolmodule.utils.Gender;
import com.portal.studymate.schoolmodule.utils.StudentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
   name = "students",
   uniqueConstraints = @UniqueConstraint(
      columnNames = {"school_id", "admission_number"}
   )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "school_id", nullable = false)
   private String schoolId;

   @Column(name = "admission_number", nullable = false)
   private String admissionNumber;

   @Column(nullable = false)
   private String fullName;

   @Column(nullable = false)
   private LocalDate dateOfBirth;

   @Enumerated(EnumType.STRING)
   private Gender gender;

   @Column(nullable = false)
   private LocalDate admissionDate;

   @Column(nullable = false)
   private String parentName;

   @Column(nullable = false)
   private String parentMobile;

   @Column(nullable = false)
   private String address;

   @Enumerated(EnumType.STRING)
   private StudentStatus status;

   @CreationTimestamp
   private LocalDateTime createdAt;
}

