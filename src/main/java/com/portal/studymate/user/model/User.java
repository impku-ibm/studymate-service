package com.portal.studymate.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
   @Id
   private String id;

   @Email
   @NotBlank
   private String email;

   @NotBlank
   private String password;
   private String role;
   private boolean enabled;
   private String schoolId;
}
