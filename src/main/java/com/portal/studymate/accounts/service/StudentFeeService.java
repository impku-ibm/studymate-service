package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.responses.StudentFeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentFeeService {
   Page<StudentFeeResponse> getStudentFees(Long studentId, Pageable pageable);
   Page<StudentFeeResponse> getStudentFeesByClass(Long classId, Long academicYearId, Pageable pageable);
   void generateFeesForEnrollment(Long enrollmentId);
   void generateFeesForClass(Long academicYearId, Long classId);
   void generateExamFeesForAllStudents(Long examId);
   void generateAdmissionFeeForStudent(Long studentId);
}
