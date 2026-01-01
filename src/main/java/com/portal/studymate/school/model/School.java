package com.portal.studymate.school.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
   name = "school",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = "school_code")
   }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class School {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String name;

   @Column(nullable = false)
   private String board;

   @Column(nullable = false, columnDefinition = "TEXT")
   private String address;

   @Column(nullable = false)
   private String city;

   @Column(nullable = false)
   private String state;

   @Column(name = "academic_start_month", nullable = false)
   private String academicStartMonth;

   @Column(name = "school_code", nullable = false, unique = true)
   private String schoolCode;

   private boolean active = true;

   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();
}

