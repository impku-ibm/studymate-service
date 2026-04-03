package com.portal.studymate.classmanagement.service.impl;

import com.portal.studymate.classmanagement.dto.CreateSectionRequest;
import com.portal.studymate.classmanagement.dto.SectionResponse;
import com.portal.studymate.classmanagement.dto.UpdateSectionRequest;
import com.portal.studymate.classmanagement.model.ClassSectionTemplate;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.ClassSectionTemplateRepository;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.classmanagement.service.SectionService;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SectionServiceImpl implements SectionService {

   private final SchoolClassRepository classRepository;
   private final ClassSectionTemplateRepository sectionRepository;

   @Override
   public SectionResponse createSection(Long classId, CreateSectionRequest request) {
      log.info("createSection called - classId: {}, name: {}", classId, request.getName());

      SchoolClass schoolClass = classRepository.findById(classId)
                                               .orElseThrow(() ->
                                                               new ResourceNotFoundException(
                                                                  "CLASS_NOT_FOUND"
                                                               )
                                               );

      sectionRepository
         .findBySchoolClassAndName(schoolClass, request.getName())
         .ifPresent(s -> {
            throw new ConflictException(
               "SECTION_ALREADY_EXISTS",
               "Section already exists for this class"
            );
         });

      ClassSectionTemplate section = ClassSectionTemplate.builder()
                                                         .schoolClass(schoolClass)
                                                         .name(request.getName())
                                                         .active(true)
                                                         .build();

      return toResponse(sectionRepository.save(section));
   }

   @Override
   @Transactional(readOnly = true)
   public List<SectionResponse> getSectionsByClass(Long classId) {
      log.info("getSectionsByClass called - classId: {}", classId);

      SchoolClass schoolClass = classRepository.findById(classId)
                                               .orElseThrow(() ->
                                                               new ResourceNotFoundException(
                                                                  "CLASS_NOT_FOUND"
                                                               )
                                               );

      return sectionRepository.findBySchoolClass(schoolClass)
                              .stream()
                              .map(this::toResponse)
                              .toList();
   }

   @Override
   public SectionResponse updateSection(Long sectionId, UpdateSectionRequest request) {
      log.info("updateSection called - sectionId: {}", sectionId);

      ClassSectionTemplate section = sectionRepository.findById(sectionId)
                                                      .orElseThrow(() ->
                                                                      new ResourceNotFoundException(
                                                                         "SECTION_NOT_FOUND"
                                                                      )
                                                      );

      section.setName(request.getName());
      section.setActive(request.isActive());

      return toResponse(sectionRepository.save(section));
   }

   private SectionResponse toResponse(ClassSectionTemplate s) {
      return SectionResponse.builder()
                            .id(s.getId())
                            .name(s.getName())
                            .active(s.isActive())
                            .build();
   }
}

