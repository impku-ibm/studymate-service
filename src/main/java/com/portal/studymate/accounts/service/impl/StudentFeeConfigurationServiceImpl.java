package com.portal.studymate.accounts.service.impl;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.accounts.dtos.requests.ConfigureStudentFeesRequest;
import com.portal.studymate.accounts.dtos.responses.StudentFeeConfigurationResponse;
import com.portal.studymate.accounts.enums.AuditAction;
import com.portal.studymate.accounts.enums.FeeType;
import com.portal.studymate.accounts.model.StudentFeeConfiguration;
import com.portal.studymate.accounts.repository.StudentFeeConfigurationRepository;
import com.portal.studymate.accounts.service.AuditService;
import com.portal.studymate.accounts.service.StudentFeeConfigurationService;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of StudentFeeConfigurationService.
 * 
 * This service manages which fee types are applicable for each student.
 * Key scenarios:
 * 1. During admission - configure initial fee types
 * 2. Mid-year changes - add/remove fee types (e.g., opt out of transport)
 * 3. Fee generation - only generate fees for configured types
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentFeeConfigurationServiceImpl implements StudentFeeConfigurationService {

   private final StudentFeeConfigurationRepository configRepository;
   private final StudentRepository studentRepository;
   private final AcademicYearRepository academicYearRepository;
   private final AuditService auditService;

   /**
    * Configure fee types for a student.
    * 
    * Logic:
    * 1. Fetch existing configurations
    * 2. For each requested fee type:
    *    - If exists and inactive → activate it
    *    - If doesn't exist → create new configuration
    * 3. For existing configurations not in request → deactivate them
    * 
    * Example: Student had TUITION + TRANSPORT, now wants TUITION + HOSTEL
    * Result: TUITION stays active, TRANSPORT deactivated, HOSTEL added
    */
   @Override
   public List<StudentFeeConfigurationResponse> configureStudentFees(
         Long studentId, 
         ConfigureStudentFeesRequest request) {
      
      // Fetch student and academic year
      Student student = studentRepository.findById(studentId)
         .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
      
      AcademicYear academicYear = academicYearRepository.findById(request.academicYearId())
         .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

      // Get existing configurations for this student and academic year
      List<StudentFeeConfiguration> existingConfigs = configRepository
         .findByStudentAndAcademicYear(student, academicYear);

      // Process each requested fee type
      for (FeeType feeType : request.feeTypes()) {
         // Check if configuration already exists
         StudentFeeConfiguration existing = existingConfigs.stream()
            .filter(config -> config.getFeeType() == feeType)
            .findFirst()
            .orElse(null);

         if (existing != null) {
            // Configuration exists - activate if inactive
            if (!existing.isActive()) {
               existing.setActive(true);
               existing.setEndDate(null); // Clear end date
               configRepository.save(existing);
               
               log.info("Reactivated fee type {} for student {}", feeType, studentId);
               auditService.logAction(
                  AuditAction.FEE_STRUCTURE_UPDATED,
                  "StudentFeeConfiguration",
                  existing.getId(),
                  "Reactivated fee type: " + feeType
               );
            }
         } else {
            // Configuration doesn't exist - create new
            StudentFeeConfiguration newConfig = new StudentFeeConfiguration();
            newConfig.setStudent(student);
            newConfig.setAcademicYear(academicYear);
            newConfig.setFeeType(feeType);
            newConfig.setActive(true);
            newConfig.setStartDate(LocalDate.now());
            
            configRepository.save(newConfig);
            
            log.info("Added new fee type {} for student {}", feeType, studentId);
            auditService.logAction(
               AuditAction.FEE_STRUCTURE_CREATED,
               "StudentFeeConfiguration",
               newConfig.getId(),
               "Added fee type: " + feeType
            );
         }
      }

      // Deactivate fee types not in the request
      for (StudentFeeConfiguration config : existingConfigs) {
         if (config.isActive() && !request.feeTypes().contains(config.getFeeType())) {
            config.setActive(false);
            config.setEndDate(LocalDate.now());
            configRepository.save(config);
            
            log.info("Deactivated fee type {} for student {}", config.getFeeType(), studentId);
            auditService.logAction(
               AuditAction.FEE_STRUCTURE_UPDATED,
               "StudentFeeConfiguration",
               config.getId(),
               "Deactivated fee type: " + config.getFeeType()
            );
         }
      }

      // Return updated configurations
      return getStudentFeeConfigurations(studentId, request.academicYearId());
   }

   /**
    * Get all fee configurations for a student.
    * Shows both active and inactive configurations for complete history.
    */
   @Override
   @Transactional(readOnly = true)
   public List<StudentFeeConfigurationResponse> getStudentFeeConfigurations(
         Long studentId, 
         Long academicYearId) {
      
      Student student = studentRepository.findById(studentId)
         .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
      
      AcademicYear academicYear = academicYearRepository.findById(academicYearId)
         .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

      List<StudentFeeConfiguration> configs = configRepository
         .findByStudentAndAcademicYear(student, academicYear);

      return configs.stream()
         .map(config -> new StudentFeeConfigurationResponse(
            config.getId(),
            config.getFeeType(),
            config.isActive(),
            config.getStartDate(),
            config.getEndDate(),
            StudentFeeConfigurationResponse.determineStatus(config.isActive(), config.getEndDate())
         ))
         .collect(Collectors.toList());
   }

   /**
    * Deactivate a specific fee type.
    * Used when student opts out mid-year (e.g., stops using transport).
    */
   @Override
   public void deactivateFeeType(Long studentId, Long academicYearId, FeeType feeType) {
      Student student = studentRepository.findById(studentId)
         .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
      
      AcademicYear academicYear = academicYearRepository.findById(academicYearId)
         .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

      StudentFeeConfiguration config = configRepository
         .findByStudentAndAcademicYearAndFeeType(student, academicYear, feeType)
         .orElseThrow(() -> new ResourceNotFoundException("Fee configuration not found"));

      config.setActive(false);
      config.setEndDate(LocalDate.now());
      configRepository.save(config);

      log.info("Deactivated fee type {} for student {}", feeType, studentId);
      auditService.logAction(
         AuditAction.FEE_STRUCTURE_UPDATED,
         "StudentFeeConfiguration",
         config.getId(),
         "Deactivated fee type: " + feeType
      );
   }

   /**
    * Activate a previously deactivated fee type.
    * Used when student opts back in (e.g., starts using transport again).
    */
   @Override
   public void activateFeeType(Long studentId, Long academicYearId, FeeType feeType) {
      Student student = studentRepository.findById(studentId)
         .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
      
      AcademicYear academicYear = academicYearRepository.findById(academicYearId)
         .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

      StudentFeeConfiguration config = configRepository
         .findByStudentAndAcademicYearAndFeeType(student, academicYear, feeType)
         .orElseThrow(() -> new ResourceNotFoundException("Fee configuration not found"));

      config.setActive(true);
      config.setEndDate(null); // Clear end date
      configRepository.save(config);

      log.info("Activated fee type {} for student {}", feeType, studentId);
      auditService.logAction(
         AuditAction.FEE_STRUCTURE_UPDATED,
         "StudentFeeConfiguration",
         config.getId(),
         "Activated fee type: " + feeType
      );
   }
}
