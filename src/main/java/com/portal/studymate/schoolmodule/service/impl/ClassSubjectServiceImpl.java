package com.portal.studymate.schoolmodule.service.impl;

import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.schoolmodule.dtos.AssignSubjectRequest;
import com.portal.studymate.schoolmodule.dtos.ClassSubjectResponse;
import com.portal.studymate.schoolmodule.model.ClassSubject;
import com.portal.studymate.schoolmodule.repository.ClassSubjectRepository;
import com.portal.studymate.schoolmodule.repository.SectionRepository;
import com.portal.studymate.schoolmodule.service.AcademicYearService;
import com.portal.studymate.schoolmodule.service.ClassSubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassSubjectServiceImpl implements ClassSubjectService {

   private final ClassSubjectRepository repository;
   private final JwtContextService jwtContextService;
   private final AcademicYearService academicYearService;
   private final SectionRepository sectionRepository;

   @Override
   @Transactional
   public void assignSubjects(
      Long classId,
      Long sectionId,
      List<AssignSubjectRequest> subjects
   ) {
      String schoolId = jwtContextService.getSchoolId();
      Long academicYearId =
         academicYearService.getActive(schoolId).getId();

      for (AssignSubjectRequest req : subjects) {

         boolean exists =
            repository.existsByAcademicYearIdAndClassIdAndSectionIdAndSubjectId(
               academicYearId,
               classId,
               sectionId,
               req.getSubjectId()
            );

         if (exists) {
            log.warn("Subject {} already mapped to class {} section {}",
                     req.getSubjectId(), classId, sectionId);
            continue;
         }

         ClassSubject cs = ClassSubject.builder()
                                       .schoolId(schoolId)
                                       .academicYearId(academicYearId)
                                       .classId(classId)
                                       .sectionId(sectionId)
                                       .subjectId(req.getSubjectId())
                                       .weeklyPeriods(req.getWeeklyPeriods())
                                       .optional(req.isOptional())
                                       .build();

         repository.save(cs);
      }

      log.info("Subjects assigned to class {} section {}", classId, sectionId);
   }

   @Override
   public List<ClassSubjectResponse> getSubjects(
      Long classId,
      Long sectionId
   ) {
      String schoolId = jwtContextService.getSchoolId();
      Long academicYearId =
         academicYearService.getActive(schoolId).getId();

      return repository
                .findByAcademicYearIdAndClassIdAndSectionId(
                   academicYearId,
                   classId,
                   sectionId
                )
                .stream()
                .map(cs -> ClassSubjectResponse.builder()
                                               .id(cs.getId())
                                               .subjectId(cs.getSubjectId())
                                               .weeklyPeriods(cs.getWeeklyPeriods())
                                               .optional(cs.isOptional())
                                               .build()
                )
                .toList();
   }

   public boolean isSubjectAssigned(
      Long classId,
      String section,
      Long subjectId
   ) {

      // agar sectionId use kar rahe ho
      Long sectionId = getSectionId(classId, section);

      return repository.existsByClassIdAndSectionIdAndSubjectId(
         classId,
         sectionId,
         subjectId
      );
   }

   // helper
   private Long getSectionId(Long classId, String sectionName) {
      // SectionRepository se lookup
      // ya ClassService se
      return sectionRepository
                .findByClassIdAndName(classId, sectionName)
                .orElseThrow(() ->
                                new IllegalArgumentException("Invalid section"))
                .getId();
   }
}
