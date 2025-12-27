package com.portal.studymate.schoolmodule.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(
   name = "classes",
   uniqueConstraints = @UniqueConstraint(
      columnNames = {"school_id", "class_name"}
   )
)
@Data
public class ClassEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String schoolId;

   private String className; // Nursery, LKG, Class 1...
}

