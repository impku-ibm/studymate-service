package com.portal.studymate.schoolmodule.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
   name = "teachers",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"school_id", "email"})
   }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "teacher_code", nullable = false)
   private String teacherCode; // T001

   @Column(name = "school_id", nullable = false)
   private String schoolId;

   @Column(nullable = false)
   private String fullName;

   @Column(nullable = false)
   private String email;

   @Column(nullable = false)
   private String mobileNumber;

   @Column(nullable = false)
   private String qualification;

   @Column(length = 500)
   private String notes;

   @Enumerated(EnumType.STRING)
   private TeacherStatus status; // ACTIVE / INACTIVE

   @Column(nullable = false)
   private boolean deleted = false;

   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   @PrePersist
   void onCreate() {
      createdAt = LocalDateTime.now();
      updatedAt = createdAt;
   }

   @PreUpdate
   void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
}

