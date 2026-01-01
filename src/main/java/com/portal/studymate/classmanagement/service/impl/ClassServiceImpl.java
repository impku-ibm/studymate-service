package com.portal.studymate.classmanagement.service.impl;

import com.portal.studymate.classmanagement.dto.ClassResponse;
import com.portal.studymate.classmanagement.dto.CreateClassRequest;
import com.portal.studymate.classmanagement.dto.UpdateClassRequest;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.classmanagement.service.ClassService;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassServiceImpl implements ClassService {

   private final SchoolClassRepository classRepository;

   @Override
   public ClassResponse createClass(CreateClassRequest request) {

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

      School school = SchoolContext.getSchool();

      return classRepository.findBySchool(school)
                            .stream()
                            .map(this::toResponse)
                            .toList();
   }

   @Override
   public ClassResponse updateClass(Long classId, UpdateClassRequest request) {

      SchoolClass schoolClass = classRepository.findById(classId)
                                               .orElseThrow(() ->
                                                               new ResourceNotFoundException(
                                                                  "CLASS_NOT_FOUND",
                                                                  "Class not found"
                                                               )
                                               );

      schoolClass.setName(request.getName());
      schoolClass.setActive(request.isActive());

      return toResponse(classRepository.save(schoolClass));
   }

   private ClassResponse toResponse(SchoolClass c) {
      return ClassResponse.builder()
                          .id(c.getId())
                          .name(c.getName())
                          .active(c.isActive())
                          .build();
   }
}
