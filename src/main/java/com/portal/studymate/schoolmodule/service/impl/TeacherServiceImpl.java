package com.portal.studymate.schoolmodule.service.impl;

import com.portal.studymate.auth.service.AuthService;
import com.portal.studymate.common.exception.TeacherNotFoundException;
import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.schoolmodule.dtos.teacher.CreateTeacherRequest;
import com.portal.studymate.schoolmodule.dtos.teacher.TeacherResponse;
import com.portal.studymate.schoolmodule.dtos.teacher.UpdateTeacherRequest;
import com.portal.studymate.schoolmodule.model.Teacher;
import com.portal.studymate.schoolmodule.model.TeacherStatus;
import com.portal.studymate.schoolmodule.repository.TeacherRepository;
import com.portal.studymate.schoolmodule.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements TeacherService {

   private final TeacherRepository teacherRepository;
   private final JwtContextService jwtContextService;
   private final AuthService authService;

   // ➕ CREATE
   @Override
   public void createTeacher(CreateTeacherRequest req) {

      String schoolId = jwtContextService.getSchoolId();
      log.info("Creating teacher {} for school {}", req.getEmail(), schoolId);

      // 1️⃣ Create login user (Mongo)
      authService.createTeacherUser(
         req.getEmail(),
         req.getFullName(),
         req.getMobileNumber(),
         schoolId
      );

      // 2️⃣ Create teacher profile (Postgres)
      Teacher teacher = Teacher.builder()
                               .teacherCode(generateTeacherCode(schoolId))
                               .schoolId(schoolId)
                               .fullName(req.getFullName())
                               .email(req.getEmail())
                               .mobileNumber(req.getMobileNumber())
                               .qualification(req.getQualification())
                               .notes(req.getNotes())
                               .status(TeacherStatus.ACTIVE)
                               .deleted(false)
                               .build();

      teacherRepository.save(teacher);
   }

   // 📋 LIST
   @Override
   public List<TeacherResponse> getAllTeachers() {

      String schoolId = jwtContextService.getSchoolId();

      return teacherRepository.findBySchoolIdAndDeletedFalse(schoolId)
                              .stream()
                              .map(this::toResponse)
                              .toList();
   }

   // 👁️ VIEW
   @Override
   public TeacherResponse getTeacherById(Long teacherId) {

      String schoolId = jwtContextService.getSchoolId();

      Teacher teacher = teacherRepository
                           .findByIdAndSchoolId(teacherId, schoolId)
                           .orElseThrow(() ->
                                           new TeacherNotFoundException("Teacher not found"));

      return toResponse(teacher);
   }

   // ✏️ UPDATE
   @Override
   public void updateTeacher(Long teacherId, UpdateTeacherRequest req) {

      String schoolId = jwtContextService.getSchoolId();

      Teacher teacher = teacherRepository
                           .findByIdAndSchoolId(teacherId, schoolId)
                           .orElseThrow(() ->
                                           new TeacherNotFoundException("Teacher not found"));

      teacher.setFullName(req.getFullName());
      teacher.setMobileNumber(req.getMobileNumber());
      teacher.setQualification(req.getQualification());
      teacher.setNotes(req.getNotes());
      teacher.setStatus(req.getStatus());

      teacherRepository.save(teacher);

      log.info("Teacher {} updated for school {}", teacherId, schoolId);
   }

   // 🚫 DEACTIVATE
   @Override
   public void deactivateTeacher(Long teacherId) {

      String schoolId = jwtContextService.getSchoolId();

      Teacher teacher = teacherRepository
                           .findByIdAndSchoolId(teacherId, schoolId)
                           .orElseThrow(() ->
                                           new TeacherNotFoundException("Teacher not found"));

      teacher.setStatus(TeacherStatus.INACTIVE);
      teacher.setDeleted(true);

      teacherRepository.save(teacher);

      log.info("Teacher {} deactivated for school {}", teacherId, schoolId);
   }

   // 🔁 Mapper
   private TeacherResponse toResponse(Teacher teacher) {
      return TeacherResponse.builder()
                            .id(teacher.getId())
                            .teacherCode(teacher.getTeacherCode())
                            .fullName(teacher.getFullName())
                            .email(teacher.getEmail())
                            .mobileNumber(teacher.getMobileNumber())
                            .qualification(teacher.getQualification())
                            .status(teacher.getStatus())
                            .build();
   }

   // 🔢 Code generator
   private String generateTeacherCode(String schoolId) {
      long count = teacherRepository.countTeachers(schoolId) + 1;
      return String.format("T%03d", count);
   }
}


