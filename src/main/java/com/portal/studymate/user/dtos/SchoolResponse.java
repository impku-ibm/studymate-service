package com.portal.studymate.user.dtos;

import java.util.UUID;

public record SchoolResponse(UUID id,
                             String name,
                             String board,
                             String address) {
}
