package com.portal.studymate.classmanagement.service.impl;

import com.portal.studymate.classmanagement.dto.ClassResponse;
import com.portal.studymate.classmanagement.dto.CreateClassRequest;
import com.portal.studymate.classmanagement.dto.UpdateClassRequest;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.ClassSectionTemplateRepository;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.classmanagement.service.ClassService;
import com.portal.studymate.classsubject.repository.ClassSubjectRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.BadRequestException;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.student.repository.StudentEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClassServiceImpl implements ClassService {

   private final SchoolClassRepository classRepository;
   private final ClassSectionTemplateRepository sectionRepository;
   private final ClassSubjectRepository classSubjectRepository;

   @Override
   public ClassResponse createClass(CreateClassRequest request) {
      log.info("createClass called - name: {}", request.getName());

      School school = SchoolContext.getSchool();

      classRepository.findBySchoolAndName(school, request.getName())
                     .ifPresent(c -> {
                        throw new ConflictException(
                           "CLASS_ALREADY_EXISTS",
                           "Class already exists"
                        );
                     });

      SchoolClass schoolClass = SchoolClass.builder()
                                           .school(school)
                                           .name(request.getName())
                                           .active(true)
                                           .build();

      return toResponse(classRepository.save(schoolClass));
   }

   @Override
   @Transactional(readOnly = true)
   public List<ClassResponse> getAllClasses() {
      log.info("getAllClasses called");

      School school = SchoolContext.getSchool();

      return classRepository.findBySchool(school)
                            .stream()
                            .map(this::toResponse)
                            .toList();
   }

   @Override
   public ClassResponse updateClass(Long classId, UpdateClassRequest request) {
      log.info("updateClass called - classId: {}", classId);

      SchoolClass schoolClass = classRepository.findById(classId)
                                               .orElseThrow(() ->
                                                               new ResourceNotFoundException(
                                                                  "CLASS_NOT_FOUND"
                                                               )
                                               );

      schoolClass.setName(request.getName());
      schoolClass.setActive(request.isActive());

      return toResponse(classRepository.save(schoolClass));
   }

   @Override
   public void deleteClass(Long classId) {
      log.info("deleteClass called - classId: {}", classId);
      SchoolClass schoolClass = classRepository.findById(classId)
         .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

      // Delete child sections first
      var sections = sectionRepository.findBySchoolClass(schoolClass);
      if (!sections.isEmpty()) {
         log.info("Deleting {} sections for class {}", sections.size(), schoolClass.getName());
         sectionRepository.deleteAll(sections);
      }

      // Delete class-subject mappings
      try {
         var mappings = classSubjectRepository.findBySchoolClass(schoolClass);
         if (!mappings.isEmpty()) {
            log.info("Deleting {} class-subject mappings for class {}", mappings.size(), schoolClass.getName());
            classSubjectRepository.deleteAll(mappings);
         }
      } catch (Exception e) {
         log.warn("Could not delete class-subject mappings: {}", e.getMessage());
      }

      try {
         classRepository.delete(schoolClass);
         log.info("Class deleted: {}", schoolClass.getName());
      } catch (Exception e) {
         throw new BadRequestException("CLASS_HAS_DEPENDENCIES",
            "Cannot delete class — it has students enrolled, fee structures, or other dependencies. Remove them first.");
      }
   }


   private ClassResponse toResponse(SchoolClass c) {
      return ClassResponse.builder()
                          .id(c.getId())
                          .name(c.getName())
                          .active(c.isActive())
                          .build();
   }
}
