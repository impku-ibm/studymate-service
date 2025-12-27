package com.portal.studymate.schoolmodule.service.impl;

import com.portal.studymate.common.exception.TeacherNotFoundException;
import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.schoolmodule.dtos.teacher.AssignTeacherRequest;
import com.portal.studymate.schoolmodule.dtos.teacher.TeacherAssignmentResponse;
import com.portal.studymate.schoolmodule.model.Teacher;
import com.portal.studymate.schoolmodule.model.TeacherAssignment;
import com.portal.studymate.schoolmodule.repository.TeacherAssignmentRepository;
import com.portal.studymate.schoolmodule.repository.TeacherRepository;
import com.portal.studymate.schoolmodule.service.AcademicYearService;
import com.portal.studymate.schoolmodule.service.ClassSubjectService;
import com.portal.studymate.schoolmodule.service.SubjectMasterService;
import com.portal.studymate.schoolmodule.service.TeacherAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

   private final TeacherAssignmentRepository repository;
   private final TeacherRepository teacherRepository;
   private final JwtContextService jwtContextService;
   private final ClassSubjectService classSubjectService;
   private final AcademicYearService academicYearService;
   private final SubjectMasterService subjectService;

   @Override
   public void assignTeacher(AssignTeacherRequest req) {

      String schoolId = jwtContextService.getSchoolId();

      log.info("Assigning teacher={} class={} section={} subject={} year={} school={}",
               req.getTeacherId(), req.getClassId(), req.getSection(),
               req.getSubjectId(), req.getAcademicYearId(), schoolId
      );

      // 1️⃣ Validate academic year
      academicYearService.validateActiveYear(req.getAcademicYearId(), schoolId);

      // 2️⃣ Validate teacher
      Teacher teacher = teacherRepository
                           .findByIdAndSchoolId(req.getTeacherId(), schoolId)
                           .orElseThrow(() -> new TeacherNotFoundException("Teacher not found"));

      // 3️⃣ Validate subject belongs to class+section
      if (!classSubjectService.isSubjectAssigned(
         req.getClassId(),
         req.getSection(),
         req.getSubjectId())) {
         throw new IllegalArgumentException(
            "Subject is not assigned to given class & section");
      }

      // 4️⃣ Prevent duplicate assignment
      if (repository.existsByTeacherIdAndClassIdAndSectionAndSubjectIdAndAcademicYearId(
         teacher.getId(),
         req.getClassId(),
         req.getSection(),
         req.getSubjectId(),
         req.getAcademicYearId())) {
         throw new IllegalStateException("Teacher already assigned");
      }

      // 5️⃣ Save assignment
      TeacherAssignment assignment = TeacherAssignment.builder()
                                                      .schoolId(schoolId)
                                                      .teacher(teacher)
                                                      .classId(req.getClassId())
                                                      .section(req.getSection())
                                                      .subjectId(req.getSubjectId())
                                                      .academicYearId(req.getAcademicYearId())
                                                      .build();

      repository.save(assignment);

      log.info("Teacher assignment created successfully");
   }

   @Override
   public List<TeacherAssignmentResponse> getAssignments(Long academicYearId) {

      String schoolId = jwtContextService.getSchoolId();

      return repository.findBySchoolIdAndAcademicYearId(schoolId, academicYearId)
                       .stream()
                       .map(this::mapToResponse)
                       .toList();
   }

   @Override
   public void removeAssignment(Long assignmentId) {

      String schoolId = jwtContextService.getSchoolId();

      TeacherAssignment assignment = repository.findById(assignmentId)
                                               .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

      if (!assignment.getSchoolId().equals(schoolId)) {
         throw new SecurityException("Unauthorized access");
      }

      repository.delete(assignment);

      log.info("Teacher assignment {} removed", assignmentId);
   }

   // 🔁 Mapper
   private TeacherAssignmentResponse mapToResponse(TeacherAssignment a) {

      return TeacherAssignmentResponse.builder()
                                      .id(a.getId())
                                      .teacherId(a.getTeacher().getId())
                                      .teacherName(a.getTeacher().getFullName())
                                      .classId(a.getClassId())
                                      .section(a.getSection())
                                      .subjectId(a.getSubjectId())
                                      .subjectName(subjectService.getNameById(a.getSubjectId()))
                                      .academicYearId(a.getAcademicYearId())
                                      .build();
   }
}

