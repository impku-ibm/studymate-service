package com.portal.studymate.user.model;

import com.portal.studymate.common.util.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

   @Id
   private String id;

   @Indexed(unique = true)
   private String email;

   private String password;

   private Role role;

   private String schoolId;

   private boolean enabled;

   private boolean forcePasswordChange;   // ✅ THIS FIELD WAS MISSING
}

