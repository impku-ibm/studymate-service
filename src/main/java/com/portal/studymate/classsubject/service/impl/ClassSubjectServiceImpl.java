package com.portal.studymate.classsubject.service.impl;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.classsubject.dto.AssignSubjectsRequest;
import com.portal.studymate.classsubject.dto.ClassSubjectResponse;
import com.portal.studymate.classsubject.model.ClassSubject;
import com.portal.studymate.classsubject.repository.ClassSubjectRepository;
import com.portal.studymate.classsubject.service.ClassSubjectService;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.model.Subject;
import com.portal.studymate.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClassSubjectServiceImpl implements ClassSubjectService {

   private final ClassSubjectRepository classSubjectRepository;
   private final SchoolClassRepository classRepository;
   private final SubjectRepository subjectRepository;
   private final AcademicYearRepository academicYearRepository;

   @Override
   public List<ClassSubjectResponse> assignSubjects(AssignSubjectsRequest request) {
      log.info("assignSubjects called - classId: {}, subjectCount: {}", request.getClassId(), request.getSubjectIds().size());

      School school = SchoolContext.getSchool();

      AcademicYear activeYear = academicYearRepository
                                   .findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE)
                                   .orElseThrow(() ->
                                                   new ResourceNotFoundException(
                                                      "ACTIVE_ACADEMIC_YEAR_NOT_FOUND"
                                                   )
                                   );

      SchoolClass schoolClass = classRepository.findById(request.getClassId())
                                               .orElseThrow(() ->
                                                               new ResourceNotFoundException(
                                                                  "CLASS_NOT_FOUND"
                                                               )
                                               );

      List<ClassSubjectResponse> responses = new ArrayList<>();

      for (Long subjectId : request.getSubjectIds()) {

         Subject subject = subjectRepository.findById(subjectId)
                                            .orElseThrow(() ->
                                                            new ResourceNotFoundException(
                                                               "SUBJECT_NOT_FOUND"
                                                            )
                                            );

         if (classSubjectRepository
                 .existsByAcademicYearAndSchoolClassAndSubject(
                    activeYear, schoolClass, subject)) {
            log.info("Mapping already exists for class {} and subject {}, skipping", schoolClass.getName(), subject.getName());
            continue;
         }

         ClassSubject cs = ClassSubject.builder()
                                          .academicYear(activeYear)
                                          .schoolClass(schoolClass)
                                          .subject(subject)
                                          .active(true)
                                          .build();

            responses.add(
               toResponse(classSubjectRepository.save(cs))
            );
         }

      return responses;
   }

   @Override
   @Transactional(readOnly = true)
   public List<ClassSubjectResponse> getSubjectsForClass(Long classId) {
      log.info("getSubjectsForClass called - classId: {}", classId);

      School school = SchoolContext.getSchool();

      AcademicYear activeYear = academicYearRepository
                                   .findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE)
                                   .orElseThrow(() ->
                                                   new ResourceNotFoundException(
                                                      "ACTIVE_ACADEMIC_YEAR_NOT_FOUND"
                                                   )
                                   );

      SchoolClass schoolClass = classRepository.findById(classId)
                                               .orElseThrow(() ->
                                                               new ResourceNotFoundException(
                                                                  "CLASS_NOT_FOUND"
                                                               )
                                               );

      return classSubjectRepository
                .findByAcademicYearAndSchoolClass(activeYear, schoolClass)
                .stream()
                .map(this::toResponse)
                .toList();
   }

   @Override
   public void removeMapping(Long id) {
      log.info("removeMapping called - id: {}", id);
      ClassSubject cs = classSubjectRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Class-subject mapping not found"));
      classSubjectRepository.delete(cs);
      log.info("Removed class-subject mapping: {}", id);
   }


   private ClassSubjectResponse toResponse(ClassSubject cs) {
      return ClassSubjectResponse.builder()
                                 .id(cs.getId())
                                 .subjectId(cs.getSubject().getId())
                                 .subjectName(cs.getSubject().getName())
                                 .subjectCode(cs.getSubject().getCode())
                                 .build();
   }
}

