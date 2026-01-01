package com.portal.studymate.common.context;

import com.portal.studymate.school.model.School;
import org.springframework.stereotype.Component;

@Component
public class SchoolContext {

   private static final ThreadLocal<School> CURRENT_SCHOOL = new ThreadLocal<>();

   public static void setSchool(School school) {
      CURRENT_SCHOOL.set(school);
   }

   public static School getSchool() {
      return CURRENT_SCHOOL.get();
   }

   public static void clear() {
      CURRENT_SCHOOL.remove();
   }
}

