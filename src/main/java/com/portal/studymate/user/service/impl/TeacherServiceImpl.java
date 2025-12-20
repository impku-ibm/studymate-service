package com.portal.studymate.user.service.impl;

import com.portal.studymate.common.exception.SchoolNotFoundException;
import com.portal.studymate.common.exception.TeacherNotFoundException;
import com.portal.studymate.user.dtos.AssignTeacherRequest;
import com.portal.studymate.user.dtos.CreateTeacherRequest;
import com.portal.studymate.user.model.Teacher;
import com.portal.studymate.user.model.TeacherAssignment;
import com.portal.studymate.user.repository.SchoolRepository;
import com.portal.studymate.user.repository.TeacherAssignmentRepository;
import com.portal.studymate.user.repository.TeacherRepository;
import com.portal.studymate.user.service.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TeacherServiceImpl implements TeacherService {
   private final TeacherRepository teacherRepo;
   private final TeacherAssignmentRepository assignmentRepo;
   private final SchoolRepository schoolRepository;

   public Teacher addTeacher(UUID schoolId, CreateTeacherRequest req) {
      log.info("Adding teacher - SchoolId: {}, Name: {}, Qualification: {}", 
               schoolId, req.name(), req.qualification());
      
      // Validate school exists
      if (!schoolRepository.existsById(schoolId)) {
         log.error("School not found - SchoolId: {}", schoolId);
         throw new SchoolNotFoundException("School not found with ID: " + schoolId);
      }
      
      try {
         Teacher teacher = Teacher.builder()
                                 .schoolId(schoolId)
                                 .name(req.name())
                                 .qualification(req.qualification())
                                 .active(true)
                                 .build();
         
         Teacher savedTeacher = teacherRepo.save(teacher);
         log.info("Teacher added successfully - ID: {}, SchoolId: {}, Name: {}", 
                  savedTeacher.getId(), schoolId, req.name());
         return savedTeacher;
      } catch (Exception e) {
         log.error("Failed to add teacher - SchoolId: {}, Name: {}, Error: {}", 
                   schoolId, req.name(), e.getMessage(), e);
         throw new RuntimeException("Failed to add teacher: " + e.getMessage(), e);
      }
   }

   public TeacherAssignment assign(AssignTeacherRequest req) {
      log.info("Assigning teacher - TeacherId: {}, ClassRoomId: {}, SubjectId: {}", 
               req.teacherId(), req.classRoomId(), req.subjectId());
      try {
         // Validate teacher exists
         if (!teacherRepo.existsById(req.teacherId())) {
            log.error("Teacher not found - TeacherId: {}", req.teacherId());
            throw new TeacherNotFoundException("Teacher not found with ID: " + req.teacherId());
         }
         
         TeacherAssignment assignment = TeacherAssignment.builder()
                                                         .teacherId(req.teacherId())
                                                         .classRoomId(req.classRoomId())
                                                         .subjectId(req.subjectId())
                                                         .build();
         
         TeacherAssignment savedAssignment = assignmentRepo.save(assignment);
         log.info("Teacher assigned successfully - ID: {}, TeacherId: {}, ClassRoomId: {}, SubjectId: {}", 
                  savedAssignment.getId(), req.teacherId(), req.classRoomId(), req.subjectId());
         return savedAssignment;
      } catch (Exception e) {
         log.error("Failed to assign teacher - TeacherId: {}, ClassRoomId: {}, SubjectId: {}, Error: {}", 
                   req.teacherId(), req.classRoomId(), req.subjectId(), e.getMessage(), e);
         throw e;
      }
   }
}
