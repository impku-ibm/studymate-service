package com.portal.studymate.student.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class TransferCertificateResponse {
    private Long id;
    private String tcNumber;
    private String studentName;
    private String admissionNumber;
    private LocalDate leavingDate;
    private String reasonForLeaving;
    private String conduct;
    private String remarks;
}
