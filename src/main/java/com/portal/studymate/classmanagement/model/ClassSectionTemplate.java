package com.portal.studymate.classmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
   name = "class_section_template",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"school_class_id", "name"})
   }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSectionTemplate {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "school_class_id")
   private SchoolClass schoolClass;

   @Column(nullable = false)
   private String name; // A / B / C

   private boolean active = true;
}

