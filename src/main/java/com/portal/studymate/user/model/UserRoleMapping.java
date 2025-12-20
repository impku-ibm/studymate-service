package com.portal.studymate.user.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "user_role_mappings")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserRoleId.class)
public class UserRoleMapping {

   @Id
   @Column(name = "user_id")
   private UUID userId;

   @Id
   @Column(name = "school_id")
   private UUID schoolId;

   @Id
   private String role;
}

