package com.portal.studymate.staff.dto;

import com.portal.studymate.staff.enums.StaffType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private StaffType staffType;
    private boolean active;
}
