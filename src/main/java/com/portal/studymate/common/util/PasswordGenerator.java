package com.portal.studymate.common.util;

import java.util.UUID;

public final class PasswordGenerator {
   private PasswordGenerator() {}

   public static String generate() {
      return UUID.randomUUID()
                 .toString()
                 .replace("-", "")
                 .substring(0, 8);
   }
}
