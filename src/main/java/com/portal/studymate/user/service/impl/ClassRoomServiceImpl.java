package com.portal.studymate.user.service.impl;

import com.portal.studymate.user.dtos.CreateClassRoomRequest;
import com.portal.studymate.user.model.ClassRoom;
import com.portal.studymate.user.repository.ClassRoomRepository;
import com.portal.studymate.user.service.ClassRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassRoomServiceImpl implements ClassRoomService {
   private final ClassRoomRepository repository;

   public ClassRoom create(UUID schoolId, CreateClassRoomRequest req) {
      return repository.save(
         ClassRoom.builder()
                  .schoolId(schoolId)
                  .academicYearId(req.academicYearId())
                  .className(req.className())
                  .section(req.section())
                  .build()
      );
   }
}
