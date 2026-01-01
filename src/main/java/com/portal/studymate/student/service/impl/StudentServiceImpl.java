package com.portal.studymate.student.service.impl;

import com.portal.studymate.auth.service.AuthService;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.school.model.School;
import com.portal.studymate.student.dto.CreateStudentRequest;
import com.portal.studymate.student.dto.StudentResponse;
import com.portal.studymate.student.dto.UpdateStudentRequest;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.model.StudentStatus;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

   private final StudentRepository studentRepository;
   private final AuthService authService;
   private final SchoolContext schoolContext;

   @Override
   public StudentResponse createStudent(CreateStudentRequest req) {

      School school = schoolContext.getSchool();

//      String userId = authService.createStudentUser(
//         req.getParentMobile(),
//         school.getSchoolCode()
//      );
      String userId = "STU-" + school.getSchoolCode() + "-" + System.currentTimeMillis();

      Student student = Student.builder()
                               .userId(userId)
                               .school(school)
                               .admissionNumber(generateAdmissionNo(school.getSchoolCode()))
                               .fullName(req.getFullName())
                               .dateOfBirth(req.getDateOfBirth())
                               .admissionDate(req.getAdmissionDate())
                               .parentName(req.getParentName())
                               .parentMobile(req.getParentMobile())
                               .address(req.getAddress())
                               .status(StudentStatus.ACTIVE)
                               .build();

      return map(studentRepository.save(student));
   }

   @Override
   public List<StudentResponse> getAllStudents() {
      return studentRepository.findBySchool(schoolContext.getSchool())
                              .stream().map(this::map).toList();
   }

   @Override
   public StudentResponse updateStudent(Long id, UpdateStudentRequest req) {

      Student student = studentRepository.findById(id)
                                         .orElseThrow(() -> new RuntimeException("Student not found"));

      student.setFullName(req.getFullName());
      student.setAddress(req.getAddress());
      student.setStatus(req.getStatus());

      return map(studentRepository.save(student));
   }

   private StudentResponse map(Student s) {
      return StudentResponse.builder()
                            .id(s.getId())
                            .admissionNumber(s.getAdmissionNumber())
                            .fullName(s.getFullName())
                            .parentName(s.getParentName())
                            .parentMobile(s.getParentMobile())
                            .admissionDate(s.getAdmissionDate())
                            .status(s.getStatus())
                            .build();
   }

   private String generateAdmissionNo(String schoolCode) {
      return "ADM-" + schoolCode + "-" + System.currentTimeMillis();
   }
}


