package com.portal.studymate.user.model;

import java.io.Serializable;
import java.util.UUID;

public class UserRoleId implements Serializable {
   private UUID userId;
   private UUID schoolId;
   private String role;
}
