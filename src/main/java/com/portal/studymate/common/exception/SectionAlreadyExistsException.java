package com.portal.studymate.common.exception;

public class SectionAlreadyExistsException extends BusinessException {
   public SectionAlreadyExistsException(String name) {
      super("SECTION_ALREADY_EXISTS", "Section already exists: " + name);
   }
}

