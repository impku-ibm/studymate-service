package com.portal.studymate.user.dtos;

import java.util.UUID;

public record CreateStudentRequest(UUID academicYearId,
                                   UUID classroomId,
                                   Integer rollNumber,
                                   String admissionNumber,
                                   String name) {
}
