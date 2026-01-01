package com.portal.studymate.school.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardSummaryResponse {

   private long totalStudents;
   private long totalTeachers;
   private long totalClasses;
   private String activeAcademicYear;
   private List<String> recentActivities;
}

