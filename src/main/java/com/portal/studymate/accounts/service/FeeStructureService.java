package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.requests.CreateFeeStructureRequest;
import com.portal.studymate.accounts.dtos.responses.FeeStructureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeeStructureService {
   FeeStructureResponse createFeeStructure(CreateFeeStructureRequest request);
   Page<FeeStructureResponse> getFeeStructures(Long academicYearId, Pageable pageable);
   FeeStructureResponse updateFeeStructure(Long id, CreateFeeStructureRequest request);
   void deleteFeeStructure(Long id);
   FeeStructureResponse toggleFeeStructure(Long id);
}