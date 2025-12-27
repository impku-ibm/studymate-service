package com.portal.studymate.schoolmodule.dtos;

import lombok.Data;

@Data
public class ErrorResponse {
   private String code;
   private String message;
}
