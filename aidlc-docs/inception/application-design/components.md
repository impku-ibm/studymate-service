# Application Design — Components (Revised)

## New Backend Modules

### 1. Grading Module
- **Package**: `com.portal.studymate.grading`
- **Entities**: GradingScale, GradingScaleEntry
- **Controller**: GradingController (4 endpoints)
- **Service**: GradingService

### 2. Fee Plan Module
- **Package**: `com.portal.studymate.accounts.feeplan`
- **Entities**: FeePlan, FeePlanItem
- **Controller**: FeePlanController (5 endpoints)
- **Service**: FeePlanService

### 3. Fee Scheduler
- **Package**: `com.portal.studymate.accounts.scheduler`
- **No controller** (scheduled job only)
- **Service**: FeeSchedulerService (@Scheduled)

### 4. Timetable Module
- **Package**: `com.portal.studymate.timetable`
- **Entities**: PeriodDefinition, TimetableEntry
- **Controller**: TimetableController (6 endpoints)
- **Service**: TimetableService

### 5. Promotion Module
- **Package**: `com.portal.studymate.promotion`
- **No new entities** (uses StudentEnrollment)
- **Controller**: PromotionController (1 endpoint)
- **Service**: PromotionService

### 6. Transfer Certificate
- **Package**: `com.portal.studymate.student.tc`
- **Entity**: TransferCertificate
- **Controller**: TransferCertificateController (2 endpoints)
- **Service**: TransferCertificateService

### 7. Fee Discount
- **Package**: `com.portal.studymate.accounts.discount`
- **Entity**: FeeDiscount
- **Controller**: FeeDiscountController (3 endpoints)
- **Service**: FeeDiscountService

### 8. Report Card Generator
- **Package**: `com.portal.studymate.exam.reportcard`
- **No new entities**
- **Controller**: endpoints added to ExamController
- **Service**: ReportCardService

### 9. Staff Module
- **Package**: `com.portal.studymate.staff`
- **Entities**: Staff, StaffAttendance
- **Controller**: StaffController (5 endpoints)
- **Service**: StaffService

### 10. Notification Module (Interface Only)
- **Package**: `com.portal.studymate.notification`
- **Entity**: NotificationEvent (model only, no persistence yet)
- **Service**: NotificationService (interface)

---

## New Frontend Components

### Exams Module
- `pages/Exams.jsx` — Tab container (Exam List, Schedule, Marks, Results)
- `components/exams/ExamList.jsx` — List with filters, create modal trigger
- `components/exams/CreateExamModal.jsx` — Create exam form
- `components/exams/ExamSchedule.jsx` — Schedule subjects for an exam
- `components/exams/EnterMarks.jsx` — Inline grid + CSV upload
- `components/exams/GraceMarks.jsx` — Admin grace marks entry
- `components/exams/ViewResults.jsx` — Class results table + report card download
- `api/examApi.js` — All exam API calls

### Attendance Module
- `pages/Attendance.jsx` — Tab container (Student, Teacher, Staff attendance)
- `components/attendance/MarkStudentAttendance.jsx` — Section-wise daily marking
- `components/attendance/TeacherAttendance.jsx` — Teacher attendance view + self-mark
- `components/attendance/StaffAttendance.jsx` — Staff attendance view + self-mark
- `components/attendance/AttendanceSummary.jsx` — Monthly summary
- `components/attendance/AttendanceHistory.jsx` — Date range history
- `api/attendanceApi.js` — All attendance API calls

### Staff Module
- `pages/Staff.jsx` — Staff directory + attendance
- `components/staff/StaffDirectory.jsx` — List, add, edit staff
- `components/staff/AddStaffModal.jsx` — Create staff form
- `api/staffApi.js` — All staff API calls

### Timetable Module
- `pages/Timetable.jsx` — Tab container (Period Setup, Class View, Teacher View)
- `components/timetable/PeriodSetup.jsx` — Define periods
- `components/timetable/TimetableGrid.jsx` — Class timetable grid
- `components/timetable/TeacherTimetable.jsx` — Teacher's weekly view
- `api/timetableApi.js` — All timetable API calls

### Fee Plan Components (in Accounts)
- `components/accounts/FeePlans.jsx` — Fee plan list + CRUD
- `components/accounts/CreateFeePlanModal.jsx` — Create/edit fee plan
- `components/accounts/FeeDiscounts.jsx` — Discount management
- `api/feePlanApi.js` — Fee plan API calls

### Student Portal
- `pages/student/StudentDashboard.jsx` — Overview cards
- `pages/student/StudentAttendance.jsx` — Own attendance
- `pages/student/StudentFees.jsx` — Own fees + payment history
- `pages/student/StudentResults.jsx` — Own results + report card download
- `layout/StudentLayout.jsx` — Simpler layout with student sidebar
- `api/studentPortalApi.js` — Student-specific API calls

### Teacher Portal
- `pages/teacher/TeacherDashboard.jsx` — Classes, pending marks, self-attendance
- `pages/teacher/TeacherMarksEntry.jsx` — Marks entry (screen + CSV)
- `pages/teacher/TeacherAttendancePage.jsx` — Self-attendance + class attendance
- `pages/teacher/TeacherTimetablePage.jsx` — Own timetable
- `layout/TeacherLayout.jsx` — Teacher-specific layout
