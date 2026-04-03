package com.portal.studymate.subject.service.impl;

import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.dto.CreateSubjectRequest;
import com.portal.studymate.subject.dto.SubjectResponse;
import com.portal.studymate.subject.dto.UpdateSubjectRequest;
import com.portal.studymate.subject.model.Subject;
import com.portal.studymate.subject.repository.SubjectRepository;
import com.portal.studymate.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService {

   private final SubjectRepository subjectRepository;

   @Override
   public SubjectResponse createSubject(CreateSubjectRequest request) {
      log.info("createSubject called - name: {}, code: {}", request.getName(), request.getCode());

      School school = SchoolContext.getSchool();

      subjectRepository.findBySchoolAndCode(school, request.getCode())
                       .ifPresent(s -> {
                          throw new ConflictException(
                             "SUBJECT_ALREADY_EXISTS",
                             "Subject code already exists"
                          );
                       });

      Subject subject = Subject.builder()
                               .school(school)
                               .name(request.getName())
                               .code(request.getCode())
                               .active(true)
                               .build();

      return toResponse(subjectRepository.save(subject));
   }

   @Override
   @Transactional(readOnly = true)
   public List<SubjectResponse> getAllSubjects() {
      log.info("getAllSubjects called");

      School school = SchoolContext.getSchool();

      return subjectRepository.findBySchool(school)
                              .stream()
                              .map(this::toResponse)
                              .toList();
   }

   @Override
   public SubjectResponse updateSubject(Long subjectId, UpdateSubjectRequest request) {
      log.info("updateSubject called - subjectId: {}", subjectId);

      Subject subject = subjectRepository.findById(subjectId)
                                         .orElseThrow(() ->
                                                         new ResourceNotFoundException(
                                                            "SUBJECT_NOT_FOUND"
                                                         )
                                         );

      subject.setName(request.getName());
      subject.setActive(request.isActive());

      return toResponse(subjectRepository.save(subject));
   }

   private SubjectResponse toResponse(Subject s) {
      return SubjectResponse.builder()
                            .id(s.getId())
                            .name(s.getName())
                            .code(s.getCode())
                            .active(s.isActive())
                            .build();
   }
}

