package com.portal.studymate.teachermgmt.service.impl;
import com.portal.studymate.auth.service.AuthService;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.teachermgmt.dto.CreateTeacherRequest;
import com.portal.studymate.teachermgmt.dto.TeacherResponse;
import com.portal.studymate.teachermgmt.dto.UpdateTeacherRequest;
import com.portal.studymate.teachermgmt.model.Teacher;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import com.portal.studymate.teachermgmt.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherServiceImpl implements TeacherService {

   private final TeacherRepository teacherRepository;
   private final AuthService authServiceClient;

   @Override
   public TeacherResponse createTeacher(CreateTeacherRequest req) {

      School school = SchoolContext.getSchool();
      String schoolCode = school.getSchoolCode();

      teacherRepository.findBySchoolAndEmail(school, req.getEmail())
                       .ifPresent(t -> {
                          throw new ConflictException(
                             "TEACHER_ALREADY_EXISTS",
                             "Teacher with this email already exists"
                          );
                       });

      // 1️⃣ Create login user (Auth Service)
      String userId = authServiceClient.createTeacherUser(
         req.getEmail(),
         req.getFullName(),
         req.getMobileNumber(),
         schoolCode
      );

      // 2️⃣ Create teacher domain record
      Teacher teacher = Teacher.builder()
                               .userId(userId)
                               .school(school)
                               .fullName(req.getFullName())
                               .email(req.getEmail())
                               .phone(req.getMobileNumber())
                               .teacherCode(generateTeacherCode(schoolCode))
                               .qualification(req.getQualification())
                               .notes(req.getNotes())
                               .active(true)
                               .build();

      return toResponse(teacherRepository.save(teacher));
   }

   @Override
   @Transactional(readOnly = true)
   public List<TeacherResponse> getAllTeachers() {

      School school = SchoolContext.getSchool();

      return teacherRepository.findBySchool(school)
                              .stream()
                              .map(this::toResponse)
                              .toList();
   }

   @Override
   public TeacherResponse updateTeacher(Long teacherId, UpdateTeacherRequest req) {

      Teacher teacher = teacherRepository.findById(teacherId)
                                         .orElseThrow(() ->
                                                         new ResourceNotFoundException(
                                                            "TEACHER_NOT_FOUND",
                                                            "Teacher not found"
                                                         )
                                         );

      teacher.setFullName(req.getFullName());
      teacher.setPhone(req.getMobileNumber());
      teacher.setQualification(req.getQualification());
      teacher.setNotes(req.getNotes());
      teacher.setActive(req.isActive());

      return toResponse(teacherRepository.save(teacher));
   }

   private TeacherResponse toResponse(Teacher t) {
      return TeacherResponse.builder()
                            .id(t.getId())
                            .fullName(t.getFullName())
                            .email(t.getEmail())
                            .mobileNumber(t.getPhone())
                            .teacherCode(t.getTeacherCode())
                            .qualification(t.getQualification())
                            .notes(t.getNotes())
                            .active(t.isActive())
                            .build();
   }

   private String generateTeacherCode(String schoolCode) {
      return schoolCode + "-T-" + System.currentTimeMillis();
   }
}
