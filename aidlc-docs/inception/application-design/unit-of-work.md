# Units of Work

## Architecture Context
This is a monolith (single Spring Boot app + single React SPA). Units of work represent logical development phases, not deployable services. Each unit is a batch of related changes that can be built, tested, and verified together.

---

## Unit 1: Foundation — Bug Fixes + Missing Backend Endpoints
**Priority**: Critical (unblocks everything)
**Scope**: Fix existing broken code, add missing API endpoints

### Backend Changes
- Fix `SchoolOnboarding.jsx`: import useState, wire form inputs, fix endpoint `/schools` → `/school`
- Add `PATCH /academic-years/{id}/activate` to AcademicYearController
- Add `DELETE /api/v1/accounts/fee-structures/{id}` to AccountsController
- Add `PUT /api/v1/accounts/fee-structures/{id}` to AccountsController
- Add `PATCH /api/v1/accounts/fee-structures/{id}/toggle` to AccountsController
- Add `GET /api/v1/accounts/student-fees/class/{classId}` to AccountsController
- Add CORS PATCH method to SecurityConfig

### Frontend Changes
- Fix `FeeCollection.jsx`: `/api/classes` → `/classes`
- Fix `SchoolOnboarding.jsx`: useState import, form wiring, endpoint fix
- Wire `/admin/exams` route in App.jsx (empty page placeholder)
- Add ClassSubjectSetup to SchoolSetup tabs
- Fix Topbar to show actual user name

### Estimated Files: ~10

---

## Unit 2: Database Migrations + Enum Extensions
**Priority**: Critical (unblocks Units 3-10)
**Scope**: New Flyway migrations, enum changes, new entities

### New Flyway Migrations
- `V9__add_grading_tables.sql` — grading_scale, grading_scale_entry
- `V10__add_fee_plan_tables.sql` — fee_plan, fee_plan_item, add fee_plan_id to student
- `V11__add_fee_discount_table.sql` — fee_discount
- `V12__add_timetable_tables.sql` — period_definition, timetable_entry
- `V13__add_transfer_certificate_table.sql` — transfer_certificate
- `V14__add_staff_tables.sql` — staff, staff_attendance
- `V15__extend_enums.sql` — add new values to fee_type, payment_mode, exam_type; add TRANSFERRED to student status

### New Entity Classes
- GradingScale, GradingScaleEntry
- FeePlan, FeePlanItem
- FeeDiscount
- PeriodDefinition, TimetableEntry
- TransferCertificate
- Staff, StaffAttendance

### New Enums
- StaffType, FeeFrequency, DiscountType

### Enum Extensions
- ExamType: +FA1, FA2, SA1, SA2
- FeeType: +LAB, LIBRARY, SPORTS, ANNUAL, DEVELOPMENT, LATE_FEE_PENALTY, UNIFORM, BOOKS
- PaymentMode: +CHEQUE, DEMAND_DRAFT, ONLINE
- StudentStatus: +TRANSFERRED
- Role: +STAFF

### Estimated Files: ~25 (migrations + entities + enums + repositories)

---

## Unit 3: Exams UI + Enhanced Exam Backend
**Priority**: High
**Dependencies**: Unit 2

### Backend Changes
- Add to ExamController: getSchedulesForExam, getClassResults, getStudentResult
- Add CSV marks upload endpoint: `POST /api/exams/marks/upload`
- Add grace marks endpoint: `POST /api/exams/{examId}/grace-marks`
- Enhance ExamService: CSV parsing, grace marks, teacher assignment validation
- Integrate GradingService into result publishing

### Frontend Changes
- `pages/Exams.jsx` — tab container
- `components/exams/ExamList.jsx` — list with filters + create modal
- `components/exams/CreateExamModal.jsx`
- `components/exams/ExamSchedule.jsx` — schedule subjects
- `components/exams/EnterMarks.jsx` — inline grid + CSV upload
- `components/exams/GraceMarks.jsx` — admin grace marks
- `components/exams/ViewResults.jsx` — results table
- `api/examApi.js`
- Wire route in App.jsx

### Estimated Files: ~15

---

## Unit 4: Attendance UI + Teacher/Staff Self-Attendance
**Priority**: High
**Dependencies**: Unit 2

### Backend Changes
- Add teacher self-attendance endpoint: `POST /api/attendance/teacher/self`
- Add staff self-attendance endpoint: `POST /api/staff/attendance/self`
- Enhance AttendanceController with teacher attendance view endpoints

### Frontend Changes
- `pages/Attendance.jsx` — tab container (Student, Teacher, Staff)
- `components/attendance/MarkStudentAttendance.jsx`
- `components/attendance/TeacherAttendance.jsx` — view + self-mark
- `components/attendance/StaffAttendance.jsx` — view + self-mark
- `components/attendance/AttendanceSummary.jsx`
- `components/attendance/AttendanceHistory.jsx`
- `api/attendanceApi.js`
- Add Attendance link to Sidebar
- Wire route in App.jsx

### Estimated Files: ~12

---

## Unit 5: Fee Plans + Fee Enhancements + Discounts
**Priority**: High
**Dependencies**: Unit 2

### Backend Changes
- New FeePlanController (5 endpoints)
- New FeePlanService
- New FeeDiscountController (3 endpoints)
- New FeeDiscountService
- New FeeSchedulerService (@Scheduled monthly job)
- Enhance StudentFeeService: use FeePlan for generation, apply discounts
- Add fee_plan_id to Student entity
- Enhance enrollment flow: auto-generate admission + first month fees

### Frontend Changes
- `components/accounts/FeePlans.jsx` — plan list + CRUD
- `components/accounts/CreateFeePlanModal.jsx`
- `components/accounts/FeeDiscounts.jsx` — discount management
- `api/feePlanApi.js`
- Add Fee Plans and Discounts tabs to Account page
- Update student enrollment to include fee plan assignment

### Estimated Files: ~18

---

## Unit 6: Staff Module
**Priority**: Medium
**Dependencies**: Unit 2

### Backend Changes
- New StaffController (5 endpoints: CRUD + self-attendance)
- New StaffService
- StaffRepository, StaffAttendanceRepository

### Frontend Changes
- `pages/Staff.jsx` — directory + attendance tabs
- `components/staff/StaffDirectory.jsx`
- `components/staff/AddStaffModal.jsx`
- `api/staffApi.js`
- Add Staff link to Sidebar
- Wire route in App.jsx

### Estimated Files: ~10

---

## Unit 7: Timetable Module
**Priority**: Medium
**Dependencies**: Unit 2

### Backend Changes
- New TimetableController (6 endpoints)
- New TimetableService (with conflict detection)
- PeriodDefinitionRepository, TimetableEntryRepository

### Frontend Changes
- `pages/Timetable.jsx` — tab container
- `components/timetable/PeriodSetup.jsx`
- `components/timetable/TimetableGrid.jsx` — class timetable
- `components/timetable/TeacherTimetable.jsx` — teacher view
- `api/timetableApi.js`
- Add Timetable link to Sidebar
- Wire route in App.jsx

### Estimated Files: ~12

---

## Unit 8: Student Promotion + Transfer Certificate
**Priority**: Medium
**Dependencies**: Unit 2

### Backend Changes
- New PromotionController (1 endpoint)
- New PromotionService
- New TransferCertificateController (2 endpoints)
- New TransferCertificateService (with PDF generation)
- Add OpenPDF dependency to build.gradle

### Frontend Changes
- Add Promotion tab/section to Students page
- `components/students/BulkPromotionModal.jsx`
- Add TC action button to StudentDirectory
- `components/students/GenerateTCModal.jsx`

### Estimated Files: ~10

---

## Unit 9: Grading System + PDF Report Cards
**Priority**: Medium
**Dependencies**: Unit 3 (needs exam results)

### Backend Changes
- New GradingController (4 endpoints)
- New GradingService
- New ReportCardService (PDF generation with OpenPDF)
- Add report card download endpoint to ExamController
- Seed default CBSE grading scale on school creation

### Frontend Changes
- Add Grading Scale setup to SchoolSetup page
- `components/setup/GradingScaleSetup.jsx`
- `components/setup/GradingScaleModal.jsx`
- Add "Download Report Card" button to ViewResults component

### Estimated Files: ~10

---

## Unit 10: Student Portal + Teacher Portal
**Priority**: Medium
**Dependencies**: Units 3, 4, 5 (needs exams, attendance, fees data)

### Backend Changes
- New StudentPortalController (dashboard endpoint aggregating attendance, fees, results)
- Role-based route guards in SecurityConfig

### Frontend Changes
- `layout/StudentLayout.jsx` — simpler sidebar for students
- `pages/student/StudentDashboard.jsx`
- `pages/student/StudentAttendance.jsx`
- `pages/student/StudentFees.jsx`
- `pages/student/StudentResults.jsx`
- `layout/TeacherLayout.jsx` — teacher-specific sidebar
- `pages/teacher/TeacherDashboard.jsx`
- `pages/teacher/TeacherMarksEntry.jsx`
- `pages/teacher/TeacherAttendancePage.jsx`
- `pages/teacher/TeacherTimetablePage.jsx`
- `api/studentPortalApi.js`
- Update App.jsx with `/student/*` and `/teacher/*` routes
- Update Login to redirect based on role
- `components/ProtectedRoute.jsx` enhanced with role checking

### Estimated Files: ~15

---

## Summary

| Unit | Name | Priority | Dependencies | Est. Files |
|------|------|----------|-------------|-----------|
| 1 | Bug Fixes + Missing Endpoints | Critical | None | ~10 |
| 2 | DB Migrations + Entities + Enums | Critical | Unit 1 | ~25 |
| 3 | Exams UI + Enhanced Backend | High | Unit 2 | ~15 |
| 4 | Attendance UI + Self-Attendance | High | Unit 2 | ~12 |
| 5 | Fee Plans + Discounts + Scheduler | High | Unit 2 | ~18 |
| 6 | Staff Module | Medium | Unit 2 | ~10 |
| 7 | Timetable Module | Medium | Unit 2 | ~12 |
| 8 | Promotion + Transfer Certificate | Medium | Unit 2 | ~10 |
| 9 | Grading + Report Cards | Medium | Unit 3 | ~10 |
| 10 | Student Portal + Teacher Portal | Medium | Units 3,4,5 | ~15 |
| **Total** | | | | **~137 files** |

## Execution Order
```
Phase 1: Unit 1 → Unit 2 (sequential, foundation)
Phase 2: Unit 3 + Unit 4 + Unit 5 (parallel, core features)
Phase 3: Unit 6 + Unit 7 + Unit 8 (parallel, secondary features)
Phase 4: Unit 9 (needs Unit 3 results)
Phase 5: Unit 10 (needs Units 3, 4, 5 data)
```
