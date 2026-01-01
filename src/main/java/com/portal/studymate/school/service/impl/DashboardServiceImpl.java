package com.portal.studymate.school.service.impl;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.school.dtos.DashboardSummaryResponse;
import com.portal.studymate.school.model.School;
import com.portal.studymate.school.service.DashboardService;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

   private final StudentRepository studentRepository;
   private final TeacherRepository teacherRepository;
   private final SchoolClassRepository classRepository;
   private final AcademicYearRepository academicYearRepository;

   @Override
   public DashboardSummaryResponse getSummary() {

      School school = SchoolContext.getSchool();
      if (school == null) {
         throw new IllegalStateException("School context not available");
      }

      long totalStudents =
         studentRepository.countBySchool(school);

      long totalTeachers =
         teacherRepository.countBySchool(school);

      long totalClasses =
         classRepository.countBySchool(school);

      String activeYear =
         academicYearRepository
            .findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE)
            .map(AcademicYear::getYear)
            .orElse("-");

      return DashboardSummaryResponse.builder()
                                     .totalStudents(totalStudents)
                                     .totalTeachers(totalTeachers)
                                     .totalClasses(totalClasses)
                                     .activeAcademicYear(activeYear)
                                     .recentActivities(List.of()) // for now
                                     .build();
   }
}
