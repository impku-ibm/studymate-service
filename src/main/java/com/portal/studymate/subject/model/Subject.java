package com.portal.studymate.subject.model;

import com.portal.studymate.school.model.School;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
   name = "subject",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"school_id", "code"})
   }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "school_id")
   private School school;

   @Column(nullable = false)
   private String name;

   @Column(nullable = false)
   private String code;

   private boolean active = true;
}
