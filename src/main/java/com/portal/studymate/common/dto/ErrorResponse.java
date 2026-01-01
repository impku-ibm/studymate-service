package com.portal.studymate.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

   private String errorCode;
   private String message;
   private Instant timestamp;
   private String path;
}

