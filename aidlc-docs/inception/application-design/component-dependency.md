# Application Design — Component Dependencies (Revised)

## Dependency Matrix

| Component | Depends On |
|-----------|-----------|
| GradingService | SchoolContext |
| FeePlanService | FeePlanRepository, FeePlanItemRepository, StudentRepository, SchoolContext |
| FeeSchedulerService | FeePlanService, StudentFeeService, StudentEnrollmentService, FeeDiscountService, SchoolContext |
| TimetableService | PeriodDefinitionRepo, TimetableEntryRepo, TeacherRepo, ClassService, SubjectService, SchoolContext |
| PromotionService | StudentEnrollmentService, StudentRepository, ClassService, AcademicYearService, SchoolContext |
| TransferCertificateService | StudentRepository, SchoolService, SchoolContext |
| FeeDiscountService | FeeDiscountRepository, StudentRepository, SchoolContext |
| ReportCardService | ExamService, GradingService, StudentRepository, SchoolService, SchoolContext |
| StaffService | StaffRepository, StaffAttendanceRepository, SchoolContext |
| NotificationService | (interface only) |
| ExamService (enhanced) | GradingService (NEW), TeacherAssignmentService (for marks validation) |
| StudentFeeService (enhanced) | FeePlanService (NEW), FeeDiscountService (NEW) |

## Key Data Flows

### Flow 1: Student Admission → Fee Generation
```
Admin creates Student
  -> Admin enrolls Student (POST /enrollments)
  -> Admin assigns Fee Plan (PUT /students/{id}/fee-plan)
  -> StudentEnrolledEvent published
  -> FeeGenerationListener receives event
     -> FeePlanService.getStudentPlan(studentId)
     -> StudentFeeService.generateAdmissionAndFirstMonthFees(studentId, planId)
        -> Create admission fee (ONE_TIME from plan)
        -> Create first month fees (MONTHLY items from plan)
        -> Apply FeeDiscountService discounts
        -> Save StudentFee records
```

### Flow 2: Monthly Fee Auto-Generation
```
Scheduled Job (1st of each month, 1 AM)
  -> FeeSchedulerService.generateMonthlyFeesForAllSchools()
     -> For each school:
        -> Get all active enrollments
        -> For each student:
           -> FeePlanService.getStudentPlan(studentId)
           -> Get MONTHLY fee plan items
           -> FeeDiscountService.calculateDiscountedAmount(...)
           -> StudentFeeService.createStudentFee(...)
           -> Set dueDate = 10th of current month (configurable)
```

### Flow 3: Marks Entry (Teacher via Screen)
```
Teacher opens Marks Entry page
  -> Selects Exam, Class, Section, Subject
  -> System validates: is this teacher assigned to this subject/class/section?
     -> If yes: show marks grid
     -> If no and not ADMIN: show "Not authorized"
  -> Teacher enters marks inline (student name | marks | absent | remarks)
  -> POST /api/exams/marks (bulk save)
  -> Validation: marks <= maxMarks, absent XOR marks present
```

### Flow 4: Marks Entry (Teacher via CSV)
```
Teacher opens Marks Entry page
  -> Clicks "Upload CSV"
  -> Downloads CSV template (admissionNumber, marksObtained, absent, remarks)
  -> Fills in marks offline
  -> Uploads CSV file
  -> POST /api/exams/marks/upload (multipart/form-data)
  -> Backend:
     -> Parse CSV
     -> Validate: all students exist, marks <= maxMarks, no duplicates
     -> If errors: return error list (row number, error message)
     -> If valid: bulk save ExamMarks
     -> Return success count
```

### Flow 5: Grace Marks + Result Publishing
```
Admin opens Exam detail page
  -> Sees all scheduled subjects with marks entry status
  -> Clicks "Add Grace Marks"
     -> Selects students, enters additional marks per subject
     -> POST /api/exams/{examId}/grace-marks
     -> ExamMarks.marksObtained += graceMarks (audit logged)
  -> Clicks "Publish Results"
     -> POST /api/exams/{examId}/publish
     -> For each student:
        -> Sum all subject marks → totalMarks
        -> Sum all maxMarks → maxPossibleMarks
        -> percentage = (totalMarks / maxPossibleMarks) * 100
        -> grade = GradingService.calculateGrade(schoolId, percentage)
        -> PASS if all subjects >= passMarks, else FAIL
        -> Rank by percentage within class/section
        -> Save StudentResult
     -> exam.status = PUBLISHED
     -> Results visible to students/parents
```

### Flow 6: Teacher Self-Attendance
```
Teacher logs in
  -> Dashboard shows "Mark Attendance" button (if not already marked today)
  -> Teacher clicks "Mark Attendance" → status = PRESENT
  -> POST /api/attendance/teacher/self
     -> Extract teacherId from JWT
     -> Validate: no existing record for today
     -> Save TeacherAttendance(teacherId, today, PRESENT)
  -> If teacher doesn't mark by end of day → remains unmarked (admin can review)
```

### Flow 7: Staff Self-Attendance
```
Staff member logs in (STAFF role)
  -> Dashboard shows "Mark Attendance" button
  -> Staff clicks → POST /api/staff/attendance/self
     -> Extract staffId from JWT
     -> Save StaffAttendance(staffId, today, PRESENT)
```

## Frontend Route Structure (Updated)

```
/ .......................... Landing
/login ..................... Login (all roles)
/admin ..................... DashboardLayout (ADMIN)
  /admin ................... AdminDashboard
  /admin/setup ............. SchoolSetup (Year, Classes, Sections, Subjects, ClassSubjects)
  /admin/students .......... Students (Directory, Enrollment, Fee Plan Assignment)
  /admin/teachers .......... Teachers (Directory, Assignments)
  /admin/staff ............. Staff (Directory, Attendance) — NEW
  /admin/accounts .......... Accounts (Fee Structure, Fee Plans, Fee Collection, Discounts, Reports)
  /admin/exams ............. Exams (List, Schedule, Marks, Grace Marks, Results) — NEW
  /admin/attendance ........ Attendance (Student, Teacher, Staff views) — NEW
  /admin/timetable ......... Timetable (Periods, Class view, Teacher view) — NEW
/teacher ................... TeacherLayout (TEACHER) — NEW
  /teacher ................. TeacherDashboard (classes, pending marks, attendance)
  /teacher/marks ........... Marks Entry (screen + CSV)
  /teacher/attendance ...... Self-attendance + class attendance
  /teacher/timetable ....... Own timetable view
/student ................... StudentLayout (STUDENT) — NEW
  /student ................. StudentDashboard
  /student/attendance ...... Own attendance
  /student/fees ............ Own fees
  /student/results ......... Own results + report card download
```
