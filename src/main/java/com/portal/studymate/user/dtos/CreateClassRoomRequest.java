package com.portal.studymate.user.dtos;

import java.util.UUID;

public record CreateClassRoomRequest(UUID academicYearId,
                                     String className,
                                     String section) {
}
