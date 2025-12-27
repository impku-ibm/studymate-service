package com.portal.studymate.schoolmodule.service.impl;

import com.portal.studymate.common.exception.DuplicateStudentException;
import com.portal.studymate.common.exception.StudentNotFoundException;
import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.schoolmodule.dtos.student.CreateStudentRequest;
import com.portal.studymate.schoolmodule.dtos.student.StudentResponse;
import com.portal.studymate.schoolmodule.dtos.student.UpdateStudentRequest;
import com.portal.studymate.schoolmodule.model.Student;
import com.portal.studymate.schoolmodule.repository.StudentRepository;
import com.portal.studymate.schoolmodule.service.StudentService;
import com.portal.studymate.schoolmodule.utils.StudentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

   private final StudentRepository repo;
   private final JwtContextService jwtContextService;

   /* ================= CREATE ================= */

   public StudentResponse create(CreateStudentRequest req) {

      String schoolId = jwtContextService.getSchoolId();
      log.info("Creating student [{}] for school={}", req.fullName(), schoolId);

      if (repo.existsBySchoolIdAndFullNameAndDateOfBirthAndParentMobile(
         schoolId,
         req.fullName(),
         req.dateOfBirth(),
         req.parentMobile())) {

         log.warn("Duplicate student detected: {}", req.fullName());
         throw new DuplicateStudentException("Student already exists");
      }

      Student s = new Student();
      s.setSchoolId(schoolId);
      s.setAdmissionNumber(generateAdmissionNumber(schoolId));
      s.setFullName(req.fullName());
      s.setDateOfBirth(req.dateOfBirth());
      s.setGender(req.gender());
      s.setAdmissionDate(req.admissionDate());
      s.setParentName(req.parentName());
      s.setParentMobile(req.parentMobile());
      s.setAddress(req.address());
      s.setStatus(StudentStatus.ACTIVE);

      repo.save(s);

      log.info("Student created admissionNo={}", s.getAdmissionNumber());
      return mapToResponse(s);
   }

   /* ================= UPDATE ================= */

   public StudentResponse update(Long studentId, UpdateStudentRequest req) {

      String schoolId = jwtContextService.getSchoolId();
      log.info("Updating student id={} school={}", studentId, schoolId);

      Student s = repo.findByIdAndSchoolId(studentId, schoolId)
                      .orElseThrow(() ->
                                      new StudentNotFoundException("Student not found"));

      s.setFullName(req.fullName());
      s.setDateOfBirth(req.dateOfBirth());
      s.setGender(req.gender());
      s.setParentName(req.parentName());
      s.setParentMobile(req.parentMobile());
      s.setAddress(req.address());

      repo.save(s);
      return mapToResponse(s);
   }

   /* ================= VIEW ================= */

   public StudentResponse getById(Long studentId) {

      String schoolId = jwtContextService.getSchoolId();

      Student s = repo.findByIdAndSchoolId(studentId, schoolId)
                      .orElseThrow(() ->
                                      new StudentNotFoundException("Student not found"));

      return mapToResponse(s);
   }

   /* ================= LIST (DIRECTORY) ================= */

   public List<StudentResponse> list() {

      String schoolId = jwtContextService.getSchoolId();
      log.info("Listing students for school={}", schoolId);

      return repo.findBySchoolIdOrderByCreatedAtDesc(schoolId)
                 .stream()
                 .map(this::mapToResponse)
                 .toList();
   }

   /* ================= DEACTIVATE / LEFT ================= */

   public void markLeft(Long studentId) {

      String schoolId = jwtContextService.getSchoolId();

      Student s = repo.findByIdAndSchoolId(studentId, schoolId)
                      .orElseThrow(() ->
                                      new StudentNotFoundException("Student not found"));

      s.setStatus(StudentStatus.LEFT);
      repo.save(s);

      log.info("Student {} marked as LEFT", s.getAdmissionNumber());
   }

   public Student get(Long studentId) {
      String schoolId = jwtContextService.getSchoolId();
      return repo.findByIdAndSchoolId(studentId, schoolId)
                 .orElseThrow(() ->
                                 new StudentNotFoundException("Student not found"));
   }


   private String generateAdmissionNumber(String schoolId) {
      long count = repo.countBySchoolId(schoolId) + 1;
      return "ADM-" + Year.now().getValue() + "-" +
             String.format("%04d", count);
   }

   private StudentResponse mapToResponse(Student s) {
      return new StudentResponse(
         s.getId(),
         s.getAdmissionNumber(),
         s.getFullName(),
         s.getParentName(),
         s.getParentMobile(),
         s.getAdmissionDate(),
         s.getStatus()
      );
   }

}
