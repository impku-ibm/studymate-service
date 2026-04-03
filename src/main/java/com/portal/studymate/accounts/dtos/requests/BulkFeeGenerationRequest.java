package com.portal.studymate.accounts.dtos.requests;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BulkFeeGenerationRequest(
   @NotNull Long academicYearId,
   @NotNull List<Long> classIds
) {}