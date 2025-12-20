package com.portal.studymate.user.service;

import com.portal.studymate.user.dtos.CreateClassRoomRequest;
import com.portal.studymate.user.model.ClassRoom;

import java.util.UUID;

public interface ClassRoomService {
   ClassRoom create(UUID schoolId, CreateClassRoomRequest req);

}
