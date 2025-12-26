package com.portal.studymate.user.dtos;

import lombok.Data;

@Data
public class ErrorResponse {
   private String code;
   private String message;
}
