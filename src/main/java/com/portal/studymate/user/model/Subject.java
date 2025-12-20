package com.portal.studymate.user.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "subjects")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject extends BaseEntity {

   @Column(name = "school_id", nullable = false)
   private UUID schoolId;

   @Column(nullable = false)
   private String name;

   @Column(name = "subject_code")
   private String subjectCode;

   private boolean active = true;
}
