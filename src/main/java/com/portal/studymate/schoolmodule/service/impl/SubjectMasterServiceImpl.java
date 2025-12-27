package com.portal.studymate.schoolmodule.service.impl;

import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.schoolmodule.dtos.SubjectCreateRequest;
import com.portal.studymate.schoolmodule.dtos.SubjectResponse;
import com.portal.studymate.schoolmodule.dtos.SubjectUpdateRequest;
import com.portal.studymate.schoolmodule.model.SubjectMaster;
import com.portal.studymate.schoolmodule.repository.SubjectMasterRepository;
import com.portal.studymate.schoolmodule.service.SubjectMasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectMasterServiceImpl implements SubjectMasterService {

   private final SubjectMasterRepository repository;
   private final JwtContextService jwtContextService;

   @Override
   public SubjectResponse create(SubjectCreateRequest request) {
      String schoolId = jwtContextService.getSchoolId();

      if (repository.existsBySchoolIdAndNameIgnoreCase(schoolId, request.getName())) {
         throw new IllegalArgumentException("Subject already exists");
      }

      SubjectMaster subject = SubjectMaster.builder()
                                           .schoolId(schoolId)
                                           .name(request.getName().trim())
                                           .code(request.getCode())
                                           .category(request.getCategory())
                                           .active(true)
                                           .build();

      SubjectMaster saved = repository.save(subject);
      log.info("Subject created: {} | schoolId={}", saved.getName(), schoolId);

      return map(saved);
   }

   @Override
   public List<SubjectResponse> getAllActive() {
      String schoolId = jwtContextService.getSchoolId();

      return repository.findAllBySchoolIdAndActiveTrue(schoolId)
                       .stream()
                       .map(this::map)
                       .toList();
   }

   @Override
   public SubjectResponse update(Long subjectId, SubjectUpdateRequest request) {
      String schoolId = jwtContextService.getSchoolId();

      SubjectMaster subject = repository.findByIdAndSchoolId(subjectId, schoolId)
                                        .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

      subject.setName(request.getName().trim());
      subject.setCode(request.getCode());
      subject.setCategory(request.getCategory());

      if (request.getActive() != null) {
         subject.setActive(request.getActive());
      }

      SubjectMaster updated = repository.save(subject);
      log.info("Subject updated: {} | schoolId={}", updated.getName(), schoolId);

      return map(updated);
   }

   private SubjectResponse map(SubjectMaster subject) {
      return SubjectResponse.builder()
                            .id(subject.getId())
                            .name(subject.getName())
                            .code(subject.getCode())
                            .category(subject.getCategory())
                            .active(subject.isActive())
                            .build();
   }

   public String getNameById(Long subjectId) {

      return repository
                .findByIdAndActiveTrue(subjectId)
                .map(SubjectMaster::getName)
                .orElse("Unknown");
   }
}
