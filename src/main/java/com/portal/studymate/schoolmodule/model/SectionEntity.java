package com.portal.studymate.schoolmodule.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(
   name = "sections",
   uniqueConstraints = @UniqueConstraint(
      columnNames = {"class_id", "name"}
   )
)
@Data
public class SectionEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "class_id", nullable = false)
   private ClassEntity schoolClass;

   private String name; // A, B, C
}


