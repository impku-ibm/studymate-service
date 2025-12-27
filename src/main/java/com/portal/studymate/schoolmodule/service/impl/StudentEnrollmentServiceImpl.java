package com.portal.studymate.schoolmodule.service.impl;

import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.schoolmodule.dtos.student.ChangeSectionRequest;
import com.portal.studymate.schoolmodule.dtos.student.EnrollStudentRequest;
import com.portal.studymate.schoolmodule.dtos.student.PromoteStudentRequest;
import com.portal.studymate.schoolmodule.model.AcademicYearEntity;
import com.portal.studymate.schoolmodule.model.ClassEntity;
import com.portal.studymate.schoolmodule.model.SectionEntity;
import com.portal.studymate.schoolmodule.model.StudentEnrollment;
import com.portal.studymate.schoolmodule.repository.StudentEnrollmentRepository;
import com.portal.studymate.schoolmodule.service.AcademicYearService;
import com.portal.studymate.schoolmodule.service.ClassService;
import com.portal.studymate.schoolmodule.service.SectionService;
import com.portal.studymate.schoolmodule.service.StudentEnrollmentService;
import com.portal.studymate.schoolmodule.service.StudentService;
import com.portal.studymate.schoolmodule.utils.EnrollmentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

   private final StudentEnrollmentRepository repo;
   private final StudentService studentService;
   private final AcademicYearService academicYearService;
   private final ClassService classService;
   private final SectionService sectionService;
   private final JwtContextService jwtContextService;


   @Override
   public void enroll(EnrollStudentRequest req) {
      String schoolId = jwtContextService.getSchoolId();
      AcademicYearEntity year = academicYearService.getActive(schoolId);

      if (repo.existsByStudentIdAndAcademicYearId(
         req.studentId(), year.getId())) {
         throw new IllegalStateException("Student already enrolled");
      }

      ClassEntity clazz = classService.get(req.classId());
      SectionEntity section = sectionService.get(req.sectionId(), req.classId());

      int roll = repo.nextRollNumber(
         clazz.getId(), section.getId(), year.getId());

      StudentEnrollment e = new StudentEnrollment();
      e.setSchoolId(schoolId);
      e.setStudent(studentService.get(req.studentId()));
      e.setAcademicYear(year);
      e.setSchoolClass(clazz);
      e.setSection(section);
      e.setRollNumber(roll);
      e.setStatus(EnrollmentStatus.ACTIVE);

      repo.save(e);
   }

   public void changeSection(Long enrollmentId, ChangeSectionRequest req) {

      StudentEnrollment e = repo.findById(enrollmentId)
                                .orElseThrow(() ->
                                                new IllegalArgumentException("Enrollment not found"));

      SectionEntity newSection =
         sectionService.get(req.newSectionId(),
                            e.getSchoolClass().getId());

      int roll = repo.nextRollNumber(
         e.getSchoolClass().getId(),
         newSection.getId(),
         e.getAcademicYear().getId());

      e.setSection(newSection);
      e.setRollNumber(roll);

      repo.save(e);
   }

   @Override
   @Transactional
   public void promote(Long enrollmentId, PromoteStudentRequest req) {

      StudentEnrollment current = repo.findById(enrollmentId)
                                      .orElseThrow(() ->
                                                      new IllegalArgumentException("Enrollment not found"));

      AcademicYearEntity nextYear =
         academicYearService.getNext(current.getSchoolId());

      current.setStatus(EnrollmentStatus.PROMOTED);
      repo.save(current);

      ClassEntity nextClass = classService.get(req.nextClassId());
      SectionEntity nextSection =
         sectionService.get(req.nextSectionId(),
                            req.nextClassId());

      int roll = repo.nextRollNumber(
         nextClass.getId(),
         nextSection.getId(),
         nextYear.getId());

      StudentEnrollment next = new StudentEnrollment();
      next.setSchoolId(current.getSchoolId());
      next.setStudent(current.getStudent());
      next.setAcademicYear(nextYear);
      next.setSchoolClass(nextClass);
      next.setSection(nextSection);
      next.setRollNumber(roll);
      next.setStatus(EnrollmentStatus.ACTIVE);

      repo.save(next);
   }

}
