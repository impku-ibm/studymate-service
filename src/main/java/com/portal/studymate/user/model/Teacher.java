package com.portal.studymate.user.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "teachers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity {

   @Column(name = "school_id", nullable = false)
   private UUID schoolId;

   @Column(nullable = false)
   private String name;

   private String qualification;

   private boolean active = true;
}

