package com.portal.studymate.schoolmodule.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
   name = "subject_master",
   uniqueConstraints = @UniqueConstraint(
      columnNames = {"school_id", "name"}
   )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectMaster {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "school_id", nullable = false)
   private String schoolId;

   @Column(nullable = false)
   private String name;

   private String code;

   private String category; // CORE / OPTIONAL / CO_CURRICULAR

   @Column(nullable = false)
   private boolean active = true;

   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @PrePersist
   public void onCreate() {
      createdAt = LocalDateTime.now();
      updatedAt = createdAt;
   }

   @PreUpdate
   public void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
}

