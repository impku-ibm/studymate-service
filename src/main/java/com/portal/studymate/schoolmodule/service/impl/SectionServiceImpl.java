package com.portal.studymate.schoolmodule.service.impl;

import com.portal.studymate.common.exception.ClassNotFoundException;
import com.portal.studymate.common.exception.SectionAlreadyExistsException;
import com.portal.studymate.common.exception.SectionNotFoundException;
import com.portal.studymate.schoolmodule.dtos.CreateSectionRequest;
import com.portal.studymate.schoolmodule.dtos.SectionResponse;
import com.portal.studymate.schoolmodule.model.ClassEntity;
import com.portal.studymate.schoolmodule.model.SectionEntity;
import com.portal.studymate.schoolmodule.repository.SectionRepository;
import com.portal.studymate.schoolmodule.service.ClassService;
import com.portal.studymate.schoolmodule.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectionServiceImpl implements SectionService {

   private final SectionRepository sectionRepository;
   private final ClassService classService;

   public List<SectionResponse> getSections(Long classId) {
      classService.get(classId); // validation
      return sectionRepository.findByClassId(classId)
                              .stream()
                              .map(s -> new SectionResponse(s.getId(), s.getName()))
                              .toList();
   }

   public SectionEntity get(Long sectionId, Long classId) {

      SectionEntity sec = sectionRepository.findById(sectionId)
                                           .orElseThrow(() -> new SectionNotFoundException(sectionId));

      if (!sec.getSchoolClass().getId().equals(classId)) {
         throw new IllegalStateException("Section does not belong to class");
      }
      return sec;
   }

   public void addSection(Long classId, CreateSectionRequest request) {

      ClassEntity cls = classService.get(classId);

      if (sectionRepository.existsByClassIdAndName(
         classId, request.name())) {
         throw new SectionAlreadyExistsException(request.name());
      }

      SectionEntity sec = new SectionEntity();
      sec.setSchoolClass(cls);
      sec.setName(request.name());

      sectionRepository.save(sec);
   }
}

