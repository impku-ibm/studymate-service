package com.portal.studymate.staff.dto;

import com.portal.studymate.staff.enums.StaffType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStaffRequest {
    @NotBlank
    private String fullName;
    private String email;
    private String phone;
    @NotNull
    private StaffType staffType;
}
