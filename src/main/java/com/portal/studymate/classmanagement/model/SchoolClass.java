package com.portal.studymate.classmanagement.model;

import com.portal.studymate.school.model.School;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
   name = "school_class",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"school_id", "name"})
   }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClass {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "school_id")
   private School school;

   @Column(nullable = false)
   private String name;

   private boolean active = true;

}

