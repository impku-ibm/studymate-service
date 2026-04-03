# Application Design — Services (Revised)

## New Services

### GradingService
- **Package**: `grading/service/`
- **Dependencies**: GradingScaleRepository, GradingScaleEntryRepository, SchoolContext
- **Responsibilities**: CRUD grading scales, calculate grade from percentage using school's scale
- **Called by**: ExamService during result publishing

### FeePlanService
- **Package**: `accounts/feeplan/service/`
- **Dependencies**: FeePlanRepository, FeePlanItemRepository, SchoolContext
- **Responsibilities**: CRUD fee plans, get plan items, assign plan to student
- **Called by**: StudentFeeService during fee generation, FeeSchedulerService for monthly generation

### FeeSchedulerService
- **Package**: `accounts/scheduler/`
- **Dependencies**: FeePlanService, StudentFeeService, StudentEnrollmentService, SchoolContext
- **Responsibilities**: Scheduled job (runs 1st of each month), generates monthly fees for all active students based on their assigned fee plan
- **Pattern**: `@Scheduled(cron = "0 0 1 1 * *")` — runs at 1 AM on 1st of each month
- **Logic**: For each school → for each active enrollment → get student's fee plan → generate fees for fee types with MONTHLY frequency

### TimetableService
- **Package**: `timetable/service/`
- **Dependencies**: PeriodDefinitionRepository, TimetableEntryRepository, TeacherRepository, ClassService, SubjectService, SchoolContext
- **Responsibilities**: CRUD periods and entries, conflict detection, class/teacher timetable views
- **Conflict rules**: No teacher double-booked same period+day, no class/section double-booked same period+day

### PromotionService
- **Package**: `promotion/service/`
- **Dependencies**: StudentEnrollmentService, StudentRepository, ClassService, AcademicYearService, SchoolContext
- **Responsibilities**: Bulk promote students, create new enrollments in target class/section/year

### TransferCertificateService
- **Package**: `student/tc/service/`
- **Dependencies**: StudentRepository, TransferCertificateRepository, SchoolService, SchoolContext
- **Responsibilities**: Generate TC record, update student status to TRANSFERRED, generate PDF

### FeeDiscountService
- **Package**: `accounts/discount/service/`
- **Dependencies**: FeeDiscountRepository, StudentRepository, SchoolContext
- **Responsibilities**: CRUD discounts, calculate discounted amount for a student

### ReportCardService
- **Package**: `exam/reportcard/service/`
- **Dependencies**: ExamService, GradingService, StudentRepository, SchoolService, SchoolContext
- **Responsibilities**: Generate PDF report card with school branding, subject-wise marks, grades, rank

### StaffService
- **Package**: `staff/service/`
- **Dependencies**: StaffRepository, StaffAttendanceRepository, SchoolContext
- **Responsibilities**: CRUD staff profiles, mark/view staff attendance

### NotificationService (Interface)
- **Package**: `notification/service/`
- **Dependencies**: None
- **Responsibilities**: Interface for sending notifications. Future implementations: SMS, WhatsApp, Email

---

## Enhanced Existing Services

### ExamService (Enhanced)
- **New dependencies**: GradingService
- **New methods**: getSchedulesForExam, getClassResults, uploadMarksFromCSV, addGraceMarks
- **Changes**: 
  - publishResults uses GradingService for grade calculation
  - Marks entry validates teacher assignment (assigned teacher or ADMIN role)
  - Grace marks: admin adds before publishing, stored as adjustment on ExamMarks
  - CSV upload: parse CSV, validate format, bulk-save marks

### StudentFeeService (Enhanced)
- **New dependencies**: FeePlanService, FeeDiscountService
- **New methods**: getStudentFeesByClass, generateMonthlyFees
- **Changes**: 
  - Fee generation uses student's FeePlan to determine which fee types to create
  - Applies FeeDiscount after calculating base amount
  - On enrollment: auto-generates admission fee + first month's fees

### AcademicYearService (Enhanced)
- **New methods**: activateAcademicYear (deactivates current, activates specified)

### FeeStructureService (Enhanced)
- **New methods**: updateFeeStructure, deleteFeeStructure, toggleFeeStructure

### AttendanceService (Enhanced)
- **Changes**: 
  - Teacher self-mark: teacher calls markOwnAttendance (validates JWT userId matches teacher)
  - Staff attendance: separate StaffService handles this

---

## Service Communication Map

```
Enrollment Event
  -> FeeGenerationListener
     -> FeePlanService.getStudentPlan(studentId)
     -> StudentFeeService.generateAdmissionAndFirstMonthFees(studentId, planId)

Monthly Scheduled Job (1st of month)
  -> FeeSchedulerService.generateMonthlyFees()
     -> For each school:
        -> StudentEnrollmentService.getActiveEnrollments()
        -> For each student:
           -> FeePlanService.getStudentPlan(studentId)
           -> FeeDiscountService.calculateDiscountedAmount(...)
           -> StudentFeeService.createStudentFee(...)

Marks Entry (Teacher)
  -> ExamController.enterMarks(request)
  -> ExamService.enterMarks(request)
     -> Validate: teacher is assigned to this subject/class OR has ADMIN role
     -> Save ExamMarks

Marks CSV Upload (Teacher)
  -> ExamController.uploadMarksCSV(examScheduleId, file)
  -> ExamService.uploadMarksFromCSV(examScheduleId, csvFile)
     -> Parse CSV (studentId/admissionNumber, marks, absent, remarks)
     -> Validate format and data
     -> Bulk save ExamMarks

Grace Marks (Admin)
  -> ExamController.addGraceMarks(examId, request)
  -> ExamService.addGraceMarks(examId, List<studentId, additionalMarks>)
     -> Update ExamMarks.marksObtained += graceMarks
     -> Log in audit trail

Result Publishing
  -> ExamController.publishResults(examId)
  -> ExamService.publishResults(examId)
     -> For each student in exam:
        -> Calculate totals, percentage
        -> GradingService.calculateGrade(schoolId, percentage)
        -> Determine PASS/FAIL per subject
        -> Calculate class rank
        -> Save StudentResult
     -> exam.status = PUBLISHED

Teacher Self-Attendance
  -> AttendanceController.markOwnAttendance(request)
  -> AttendanceService.markTeacherSelfAttendance(request)
     -> Extract teacherId from JWT
     -> Validate: can only mark own attendance
     -> Save TeacherAttendance

Staff Self-Attendance
  -> StaffController.markOwnAttendance(request)
  -> StaffService.markStaffSelfAttendance(request)
     -> Extract staffId from JWT
     -> Validate: can only mark own attendance
     -> Save StaffAttendance
```
