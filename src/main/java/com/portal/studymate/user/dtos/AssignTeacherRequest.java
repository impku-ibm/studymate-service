package com.portal.studymate.user.dtos;

import java.util.UUID;

public record AssignTeacherRequest(UUID teacherId,
                                   UUID classroomId,
                                   UUID subjectId) {
}
