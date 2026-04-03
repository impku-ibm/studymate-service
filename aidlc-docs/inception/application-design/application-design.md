# Application Design — Consolidated (Revised)

## Overview

This design adds 9 new backend modules and 13 new frontend components to the existing StudyMate ERP. Revised based on clarification of real-world Indian school workflows: student admission-to-fee flow, fee plans by student category, teacher marks entry with CSV upload, grace marks, staff management, and teacher self-attendance.

---

## Key Workflow Designs

### Workflow 1: Student Admission → Fee Flow
```
1. Admin creates Student (name, parent, address, DOB)
2. Admin enrolls Student into Class/Section (creates StudentEnrollment)
3. Admin assigns a Fee Plan to the student (e.g., "Hosteller Plan")
4. On enrollment, system auto-generates:
   - One-time admission fee
   - First month's fees (based on Fee Plan)
5. Scheduled job runs on 1st of each month:
   - For all active enrolled students
   - Generates monthly fees based on their assigned Fee Plan
   - Sets due date to configurable day of month
6. Dashboard shows overdue fees, unpaid students highlighted in red
7. Future: Notification sent to parents for overdue fees
```

### Workflow 2: Fee Plans (Student Categories)
```
Admin creates Fee Plans:
  - "Day Scholar Plan": TUITION, EXAM, LAB, LIBRARY, SPORTS, ANNUAL
  - "Transport Plan": TUITION, EXAM, LAB, LIBRARY, SPORTS, ANNUAL, TRANSPORT
  - "Hosteller Plan": TUITION, EXAM, LAB, LIBRARY, SPORTS, ANNUAL, HOSTEL, TRANSPORT

Each plan defines:
  - Plan name
  - List of fee types included
  - Amount per fee type (can differ from base fee structure)
  - Frequency per fee type (ONE_TIME, MONTHLY, QUARTERLY, ANNUAL)

Student is assigned one Fee Plan. Fee generation uses the plan to determine
which fees to create and at what amounts.
```

### Workflow 3: Marks Entry & Result Publishing
```
1. Admin creates Exam (e.g., "SA1 2025") and schedules it per class/section/subject
2. Each subject's assigned teacher enters marks:
   - Via screen: inline grid with student names, marks field, absent checkbox
   - Via CSV upload: download template → fill marks → upload CSV
   - Admin can also enter/override marks for any subject
3. Admin reviews marks, optionally adds grace marks to individual students
4. Admin publishes results for the entire exam at once:
   - System calculates totals, percentages, grades (from school's grading scale)
   - System ranks students within class
   - Results become visible to students/parents
5. PDF report cards available for download after publishing
```

### Workflow 4: Teacher & Staff Management
```
Teachers:
  - Created by admin (existing flow)
  - Assigned to class/section/subject (existing TeacherAssignment)
  - Timetable derived from assignments (new Timetable module)
  - Availability = free periods from timetable
  - Self-mark attendance daily (check-in via their login)

Non-Teaching Staff:
  - Separate Staff module (not mixed with Teacher entity)
  - Staff types: CLERK, ACCOUNTANT, LIBRARIAN, PEON, SECURITY, OTHER
  - Admin creates staff profiles
  - Staff self-mark attendance (same mechanism as teachers)
  - No timetable or subject assignment
```

---

## New Backend Modules

| # | Module | Package | New Entities | Endpoints | Purpose |
|---|--------|---------|-------------|-----------|---------|
| 1 | Grading | `grading/` | GradingScale, GradingScaleEntry | 4 | Multi-board configurable grading |
| 2 | Fee Plan | `accounts/feeplan/` | FeePlan, FeePlanItem | 5 | Student category-based fee plans |
| 3 | Fee Scheduler | `accounts/scheduler/` | (uses existing) | 0 (scheduled job) | Auto-generate monthly fees |
| 4 | Timetable | `timetable/` | PeriodDefinition, TimetableEntry | 6 | Period and timetable management |
| 5 | Promotion | `promotion/` | (uses existing) | 1 | Bulk student promotion |
| 6 | Transfer Certificate | `student/tc/` | TransferCertificate | 2 | TC generation and PDF |
| 7 | Fee Discount | `accounts/discount/` | FeeDiscount | 3 | Fee concessions |
| 8 | Report Card | `exam/reportcard/` | (uses existing) | 2 | PDF report card generation |
| 9 | Staff | `staff/` | Staff, StaffAttendance | 5 | Non-teaching staff management |
| 10 | Notification | `notification/` | NotificationEvent | 0 (interface) | Extensible notification abstraction |

## New Frontend Pages

| Route | Page | Role | Purpose |
|-------|------|------|---------|
| `/admin/exams` | Exams | ADMIN/TEACHER | Exam lifecycle, marks entry, results |
| `/admin/attendance` | Attendance | ADMIN/TEACHER | Student + teacher + staff attendance |
| `/admin/timetable` | Timetable | ADMIN | Period setup, class/teacher timetables |
| `/admin/staff` | Staff | ADMIN | Staff profiles and management |
| `/student` | StudentDashboard | STUDENT | Read-only dashboard |
| `/student/attendance` | StudentAttendance | STUDENT | Own attendance history |
| `/student/fees` | StudentFees | STUDENT | Own fee status and payments |
| `/student/results` | StudentResults | STUDENT | Own exam results and report cards |

## Enhanced Existing Components

| Component | Changes |
|-----------|---------|
| Student entity | Add `feePlanId` field (FK to FeePlan) |
| ExamService | Grace marks support, CSV marks upload, uses GradingService |
| ExamController | New endpoints: getSchedules, getResults, getReportCard, uploadMarksCSV |
| AccountsController | DELETE/PUT/PATCH fee structures, student-fees-by-class |
| AcademicYearController | PATCH activate endpoint |
| StudentFeeService | Uses FeePlan for generation, FeeDiscountService for discounts |
| TeacherAttendance | Self-mark flow (teacher marks own attendance via API) |
| ExamType enum | Add FA1, FA2, SA1, SA2 |
| FeeType enum | Add LAB, LIBRARY, SPORTS, ANNUAL, DEVELOPMENT, LATE_FEE_PENALTY, UNIFORM, BOOKS |
| PaymentMode enum | Add CHEQUE, DEMAND_DRAFT, ONLINE |
| StudentStatus enum | Add TRANSFERRED |
| Role enum | Add STAFF |
| Sidebar.jsx | Add Attendance, Timetable, Staff links; role-based rendering |
| App.jsx | Add all new routes + student portal routes |

## New Enums

| Enum | Values | Module |
|------|--------|--------|
| StaffType | CLERK, ACCOUNTANT, LIBRARIAN, PEON, SECURITY, OTHER | Staff |
| FeeFrequency | ONE_TIME, MONTHLY, QUARTERLY, HALF_YEARLY, ANNUAL | Fee Plan |
| DiscountType | SIBLING, MERIT_SCHOLARSHIP, RTE_QUOTA, STAFF_CHILDREN, CUSTOM | Accounts |

## New Entities Summary

| Entity | Key Fields | Module |
|--------|-----------|--------|
| GradingScale | schoolId, name, isDefault | Grading |
| GradingScaleEntry | gradingScaleId, gradeName, minPercentage, maxPercentage | Grading |
| FeePlan | schoolId, name, description, active | Fee Plan |
| FeePlanItem | feePlanId, feeType, amount, frequency | Fee Plan |
| FeeDiscount | studentId, discountType, percentage/fixedAmount, academicYearId | Accounts |
| PeriodDefinition | schoolId, periodNumber, startTime, endTime, isBreak | Timetable |
| TimetableEntry | periodDefinitionId, classId, section, subjectId, teacherId, dayOfWeek | Timetable |
| TransferCertificate | studentId, tcNumber, leavingDate, reason, conduct | Student |
| Staff | schoolId, fullName, email, phone, staffType, active | Staff |
| StaffAttendance | staffId, attendanceDate, status | Staff |

## Security Compliance Notes

- All new endpoints require authentication
- All new controllers use `@PreAuthorize` for role-based access
- Marks entry: teacher can only enter for assigned subjects (SECURITY-08 IDOR prevention); admin can override
- Student portal: student can only access own data
- Teacher/staff self-attendance: can only mark own attendance
- CSV upload: validate file size, format, content before processing (SECURITY-05 input validation)
- Scheduled job: runs with system context, no user impersonation
- Fee plan assignment: admin-only operation
- Grace marks: admin-only, logged in audit trail (SECURITY-13 data integrity)
- No new credentials or secrets introduced
- PDF generation uses safe library (OpenPDF)

## Artifact References
- Components: `components.md` (to be updated)
- Methods: `component-methods.md` (to be updated)
- Services: `services.md` (to be updated)
- Dependencies: `component-dependency.md` (to be updated)
- Clarification answers: `design-clarification-questions.md`, `design-clarification-followup.md`
