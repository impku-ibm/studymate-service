package com.portal.studymate.user.dtos;

import com.portal.studymate.user.model.School;
import org.springframework.stereotype.Component;

@Component
public class SchoolMapper {
   public School toEntity(CreateSchoolRequest req) {
      return School.builder()
                   .name(req.name())
                   .board(req.board())
                   .address(req.address())
                   .build();
   }

   public SchoolResponse toResponse(School school) {
      return new SchoolResponse(
         school.getId(),
         school.getName(),
         school.getBoard(),
         school.getAddress()
      );
   }
}
