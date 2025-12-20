package com.portal.studymate.user.service.impl;

import com.portal.studymate.common.exception.ClassRoomNotFoundException;
import com.portal.studymate.common.exception.SchoolNotFoundException;
import com.portal.studymate.user.dtos.CreateStudentRequest;
import com.portal.studymate.user.model.Student;
import com.portal.studymate.user.repository.ClassRoomRepository;
import com.portal.studymate.user.repository.SchoolRepository;
import com.portal.studymate.user.repository.StudentRepository;
import com.portal.studymate.user.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentServiceImpl implements StudentService {
   private final StudentRepository repository;
   private final SchoolRepository schoolRepository;
   private final ClassRoomRepository classRoomRepository;

   public Student addStudent(UUID schoolId, CreateStudentRequest req) {
      log.info("Adding student - SchoolId: {}, Name: {}, ClassRoomId: {}", 
               schoolId, req.name(), req.classroomId());
      
      // Validate school exists
      if (!schoolRepository.existsById(schoolId)) {
         log.error("School not found - SchoolId: {}", schoolId);
         throw new SchoolNotFoundException("School not found with ID: " + schoolId);
      }
      
      // Validate classroom exists
      if (!classRoomRepository.existsById(req.classroomId())) {
         log.error("ClassRoom not found - ClassRoomId: {}", req.classroomId());
         throw new ClassRoomNotFoundException("ClassRoom not found with ID: " + req.classroomId());
      }
      
      try {
         Student student = Student.builder()
                                 .schoolId(schoolId)
                                 .academicYearId(req.academicYearId())
                                 .classRoomId(req.classroomId())
                                 .rollNumber(req.rollNumber())
                                 .admissionNumber(req.admissionNumber())
                                 .name(req.name())
                                 .active(true)
                                 .build();
         
         Student savedStudent = repository.save(student);
         log.info("Student added successfully - ID: {}, Name: {}, SchoolId: {}", 
                  savedStudent.getId(), req.name(), schoolId);
         return savedStudent;
      } catch (Exception e) {
         log.error("Failed to add student - SchoolId: {}, Name: {}, Error: {}", 
                   schoolId, req.name(), e.getMessage(), e);
         throw new RuntimeException("Failed to add student: " + e.getMessage(), e);
      }
   }

   public List<Student> getByClass(UUID classroomId) {
      log.info("Getting students by classroom - ClassroomId: {}", classroomId);
      
      // Validate classroom exists
      if (!classRoomRepository.existsById(classroomId)) {
         log.error("ClassRoom not found - ClassroomId: {}", classroomId);
         throw new ClassRoomNotFoundException("ClassRoom not found with ID: " + classroomId);
      }
      
      try {
         List<Student> students = repository.findByClassRoomId(classroomId);
         log.info("Retrieved students successfully - ClassroomId: {}, Count: {}", 
                  classroomId, students.size());
         return students;
      } catch (Exception e) {
         log.error("Failed to get students by classroom - ClassroomId: {}, Error: {}", 
                   classroomId, e.getMessage(), e);
         throw new RuntimeException("Failed to retrieve students: " + e.getMessage(), e);
      }
   }
}
