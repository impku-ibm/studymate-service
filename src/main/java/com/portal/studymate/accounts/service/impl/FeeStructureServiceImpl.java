package com.portal.studymate.accounts.service.impl;

import com.portal.studymate.accounts.dtos.requests.CreateFeeStructureRequest;
import com.portal.studymate.accounts.dtos.responses.FeeStructureResponse;
import com.portal.studymate.accounts.enums.AuditAction;
import com.portal.studymate.accounts.model.FeeStructure;
import com.portal.studymate.accounts.repository.FeeStructureRepository;
import com.portal.studymate.accounts.service.AuditService;
import com.portal.studymate.accounts.service.FeeStructureService;
import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FeeStructureServiceImpl implements FeeStructureService {

   private final FeeStructureRepository feeStructureRepository;
   private final AcademicYearRepository academicYearRepository;
   private final SchoolClassRepository schoolClassRepository;
   private final AuditService auditService;

   @Override
   public FeeStructureResponse createFeeStructure(CreateFeeStructureRequest request) {
      log.info("createFeeStructure called - feeType: {}, classId: {}, academicYearId: {}", 
         request.feeType(), request.classId(), request.academicYearId());
      School school = SchoolContext.getSchool();
      if (school == null) {
         throw new com.portal.studymate.common.exception.BadRequestException(
            "NO_SCHOOL_CONTEXT", "School context not available. Please re-login.");
      }
      
      AcademicYear academicYear = academicYearRepository.findById(request.academicYearId())
         .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));
      
      SchoolClass schoolClass = schoolClassRepository.findById(request.classId())
         .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

      if (feeStructureRepository.findBySchoolAndAcademicYearAndSchoolClassAndFeeType(
            school, academicYear, schoolClass, request.feeType()).isPresent()) {
         throw new com.portal.studymate.common.exception.ConflictException(
            "FEE_STRUCTURE_EXISTS", "Fee structure already exists for this class and fee type");
      }

      FeeStructure feeStructure = new FeeStructure();
      feeStructure.setSchool(school);
      feeStructure.setAcademicYear(academicYear);
      feeStructure.setSchoolClass(schoolClass);
      feeStructure.setFeeType(request.feeType());
      feeStructure.setAmount(request.amount());
      feeStructure.setDueDate(request.dueDate());
      feeStructure.setActive(true);

      feeStructure = feeStructureRepository.save(feeStructure);
      
      auditService.logAction(AuditAction.FEE_STRUCTURE_CREATED, "FeeStructure", feeStructure.getId(), 
         "Created fee structure for " + schoolClass.getName() + " - " + request.feeType());

      return new FeeStructureResponse(
         feeStructure.getId(),
         academicYear.getYear(),
         schoolClass.getName(),
         feeStructure.getFeeType(),
         feeStructure.getAmount(),
         feeStructure.getDueDate(),
         feeStructure.isActive()
      );
   }

   @Override
   @Transactional(readOnly = true)
   public Page<FeeStructureResponse> getFeeStructures(Long academicYearId, Pageable pageable) {
      log.info("getFeeStructures called - academicYearId: {}", academicYearId);
      School school = SchoolContext.getSchool();
      if (school == null) {
         throw new com.portal.studymate.common.exception.BadRequestException(
            "NO_SCHOOL_CONTEXT", "School context not available. Please re-login.");
      }
      
      AcademicYear academicYear = academicYearRepository.findById(academicYearId)
         .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

      return feeStructureRepository.findBySchoolAndAcademicYear(school, academicYear, pageable)
         .map(fs -> new FeeStructureResponse(
            fs.getId(),
            fs.getAcademicYear().getYear(),
            fs.getSchoolClass().getName(),
            fs.getFeeType(),
            fs.getAmount(),
            fs.getDueDate(),
            fs.isActive()
         ));
   }

   @Override
   public FeeStructureResponse updateFeeStructure(Long id, CreateFeeStructureRequest request) {
      log.info("updateFeeStructure called - id: {}", id);
      School school = SchoolContext.getSchool();

      FeeStructure fs = feeStructureRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Fee structure not found"));

      fs.setFeeType(request.feeType());
      fs.setAmount(request.amount());
      fs.setDueDate(request.dueDate());

      fs = feeStructureRepository.save(fs);

      auditService.logAction(AuditAction.FEE_STRUCTURE_UPDATED, "FeeStructure", fs.getId(),
         "Updated fee structure: " + request.feeType());

      return new FeeStructureResponse(
         fs.getId(),
         fs.getAcademicYear().getYear(),
         fs.getSchoolClass().getName(),
         fs.getFeeType(),
         fs.getAmount(),
         fs.getDueDate(),
         fs.isActive()
      );
   }

   @Override
   public void deleteFeeStructure(Long id) {
      log.info("deleteFeeStructure called - id: {}", id);
      FeeStructure fs = feeStructureRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Fee structure not found"));

      auditService.logAction(AuditAction.FEE_STRUCTURE_DELETED, "FeeStructure", fs.getId(),
         "Deleted fee structure: " + fs.getFeeType());

      feeStructureRepository.delete(fs);
   }

   @Override
   public FeeStructureResponse toggleFeeStructure(Long id) {
      log.info("toggleFeeStructure called - id: {}", id);
      FeeStructure fs = feeStructureRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Fee structure not found"));

      fs.setActive(!fs.isActive());
      fs = feeStructureRepository.save(fs);

      auditService.logAction(AuditAction.FEE_STRUCTURE_UPDATED, "FeeStructure", fs.getId(),
         "Toggled fee structure active: " + fs.isActive());

      return new FeeStructureResponse(
         fs.getId(),
         fs.getAcademicYear().getYear(),
         fs.getSchoolClass().getName(),
         fs.getFeeType(),
         fs.getAmount(),
         fs.getDueDate(),
         fs.isActive()
      );
   }
}