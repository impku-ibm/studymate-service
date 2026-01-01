package com.portal.studymate.teachermgmt.model;

import com.portal.studymate.school.model.School;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
   name = "teacher",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"school_id", "email"}),
      @UniqueConstraint(columnNames = {"user_id"})
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

   // 🔑 Auth service user id (Mongo / Auth DB)
   @Column(name = "user_id", nullable = false, unique = true)
   private String userId;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "school_id")
   private School school;

   @Column(nullable = false)
   private String fullName;

   @Column(nullable = false)
   private String email;

   private String phone;

   // Optional, human-readable
   private String teacherCode;

   private String qualification;

   private String notes;

   private boolean active = true;
}

