package com.portal.studymate.student.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class GenerateTCRequest {
    @NotNull
    private LocalDate leavingDate;
    private String reasonForLeaving;
    private String conduct;
    private String remarks;
}
