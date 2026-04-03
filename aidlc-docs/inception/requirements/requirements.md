# StudyMate ERP — Requirements Document

## Intent Analysis

- **User Request**: Build a production-grade, full-featured School ERP for Indian schools. Reuse existing Spring Boot + React + Tailwind stack. Build missing UI (Exams, Attendance), fix existing bugs, add new modules (Student Promotion, TC, Timetable, Parent Portal), and enhance existing modules (Fees, Grading).
- **Request Type**: Enhancement + New Features + Bug Fixes
- **Scope**: System-wide (backend + frontend, multiple modules)
- **Complexity**: Complex — multi-module, multi-role, Indian school domain-specific

---

## Functional Requirements

### FR-01: Multi-Board Configurable Grading System
- Admin configures grading scale per school (CBSE 5-point, ICSE percentage, State Board, custom)
- Default: CBSE pattern with FA1, FA2, SA1, SA2 exam structure
- Grading table stored per school: grade name, min percentage, max percentage
- Grade auto-calculated from percentage during result generation

### FR-02: Configurable Class Structure
- Admin defines class names freely (Nursery, LKG, UKG, Class 1–12, or any custom name)
- No hardcoded class range — fully dynamic
- Already partially implemented via `SchoolClass` entity with `name` field

### FR-03: Configurable Academic Year
- Academic year start month configurable per school (already in `School.academicStartMonth`)
- No changes needed — current implementation is correct

### FR-04: Exams UI (NEW — Priority: High)
- Exam List page with filters (academic year, exam type, status)
- Create Exam modal (academic year, exam type, name, start/end date)
- Exam Schedule view (add class/section/subject schedules with max marks, pass marks, duration)
- Marks Entry page (teacher selects exam/class/section/subject, enters marks inline)
- Result Publishing (admin publishes, generates grades/ranks)
- Results View (student-wise and class-wise results)
- CBSE exam types as default: FA1, FA2, SA1, SA2 (add to ExamType enum)
- Wire `/admin/exams` route in App.jsx
- Create `examApi.js` for centralized API calls

### FR-05: Attendance UI (NEW — Priority: High)
- Mark Attendance page (teacher selects class/section/date, marks Present/Absent/Leave per student)
- Attendance by Date view (view section attendance for a specific date)
- Student Attendance Summary (monthly percentage, present/absent/leave counts)
- Student Attendance History (date range filter, table view)
- Add `/admin/attendance` route and sidebar link
- Create `attendanceApi.js` for centralized API calls

### FR-06: Student/Parent Portal (NEW — Priority: High)
- Separate login for students/parents (reuse existing auth with STUDENT role)
- Read-only dashboard showing: attendance %, pending fees, latest exam results
- View own attendance history and monthly summary
- View own fee status and payment history
- View own exam results and report card
- Separate route structure: `/student/dashboard`, `/student/attendance`, `/student/fees`, `/student/results`
- No edit capabilities — read-only

### FR-07: PDF Report Card Generation (NEW)
- Generate PDF report card per student per exam
- Include: school logo, school name, student name, class/section, roll number
- Subject-wise marks table: subject, max marks, obtained marks, grade
- Overall: total marks, percentage, grade, rank in class, result (Pass/Fail)
- Download button on Results View page
- Use a Java PDF library (e.g., iText or OpenPDF) on backend

### FR-08: Enhanced Fee Types
- Add to FeeType enum: LAB, LIBRARY, SPORTS, ANNUAL, DEVELOPMENT, LATE_FEE_PENALTY, UNIFORM, BOOKS
- Total fee types: 14 (existing 6 + 8 new)
- New Flyway migration to handle enum extension

### FR-09: Fee Concessions/Discounts (NEW)
- New entity: `FeeDiscount` — student, discount type (SIBLING, MERIT_SCHOLARSHIP, RTE_QUOTA, STAFF_CHILDREN, CUSTOM), percentage or fixed amount, academic year
- Admin can assign discounts to students
- Discount applied during fee generation (reduces student fee amount)
- API: CRUD for fee discounts, apply discount to student
- UI: Discount management tab in Accounts page

### FR-10: Payment Modes
- Support: CASH, UPI, CARD, BANK_TRANSFER, CHEQUE, DEMAND_DRAFT, ONLINE
- Add CHEQUE, DEMAND_DRAFT, ONLINE to PaymentMode enum
- Current offline recording approach is correct — no payment gateway integration needed now

### FR-11: Student Promotion (NEW)
- Bulk promote students from one class/section to another at academic year end
- Admin selects source class/section and target class/section
- System creates new enrollments in the new academic year
- Handle edge cases: students who failed (hold back), students who left (skip)
- API: `POST /api/promotions/bulk` with source/target class, list of student IDs
- UI: Promotion page accessible from Students or Setup

### FR-12: Transfer Certificate (TC) Generation (NEW)
- Generate TC PDF for a student leaving the school
- Include: student details, admission date, leaving date, class last attended, conduct, reason for leaving
- Mark student status as TRANSFERRED
- API: `POST /students/{id}/transfer-certificate`
- UI: Action button on Student Directory

### FR-13: Timetable Management (NEW)
- Define period structure per school (number of periods, start/end times, break periods)
- Assign subjects to periods for each class/section
- Assign teachers to periods (validate no teacher conflicts)
- View timetable: class-wise and teacher-wise views
- New entities: `PeriodDefinition`, `TimetableEntry`
- API: CRUD for period definitions, timetable entries, conflict validation
- UI: Timetable page under Setup or as a top-level sidebar item

### FR-14: Bug Fixes (Alongside Development)
- Fix SchoolOnboarding.jsx: import useState, wire form inputs to state, fix endpoint `/schools` → `/school`
- Fix FeeCollection.jsx: change `/api/classes` → `/classes`
- Add missing backend endpoints: `DELETE /api/v1/accounts/fee-structures/{id}`, `PUT /api/v1/accounts/fee-structures/{id}`, `PATCH /api/v1/accounts/fee-structures/{id}/toggle`, `PATCH /academic-years/{id}/activate`, `GET /api/v1/accounts/student-fees/class/{classId}`
- Wire `/admin/exams` route in App.jsx
- Add ClassSubjectSetup to SchoolSetup tabs
- Fix Topbar to show actual user name from JWT/localStorage

### FR-15: Notification System Design (Extensible — Not Built Now)
- Design a `Notification` entity and `NotificationService` interface
- Support future channels: SMS, WhatsApp, Email, Push
- Event-driven: attendance marked → trigger notification event
- Implementation deferred — only the interface/abstraction layer built now

---

## Non-Functional Requirements

### NFR-01: Performance
- Page load time < 2 seconds for all pages
- API response time < 500ms for standard CRUD operations
- Support 500-2000 concurrent students per school
- Frontend pagination for all list views (max 50 items per page)

### NFR-02: Security (ENFORCED — Security Extension Enabled)
- All SECURITY-01 through SECURITY-15 rules enforced as blocking constraints
- JWT validation on every request (already implemented)
- BCrypt password hashing (already implemented)
- Rate limiting on login (already implemented)
- CORS restricted to allowed origins (already implemented)
- Input validation on all API endpoints (partially implemented — enhance)
- No hardcoded credentials in source code
- Generic error messages to end users (no stack traces)
- RBAC enforced server-side with @PreAuthorize

### NFR-03: Scalability
- Single school deployment for now (500-2000 students)
- Multi-tenant architecture already in place (school context isolation)
- Design for future SaaS scaling

### NFR-04: Deployment
- Run locally for development (current setup)
- Docker Compose for local infra (Redis)
- Cloud deployment planned for later
- PostgreSQL on Neon (cloud), MongoDB on Atlas (cloud)

### NFR-05: Testing
- School-based integration testing (not property-based)
- Test with realistic Indian school data (class names, student names, fee amounts in INR)
- Test all roles: ADMIN, TEACHER, STUDENT
- Test multi-tenancy isolation

### NFR-06: Maintainability
- Consistent layered architecture across all modules
- Centralized API service files in frontend (one per module)
- Consistent error handling (toast notifications, not alert())
- Flyway migrations for all schema changes

---

## Data Model Changes Required

### New Entities
| Entity | Module | Purpose |
|--------|--------|---------|
| GradingScale | School | Configurable grading per school |
| GradingScaleEntry | School | Grade name, min%, max% per scale |
| FeeDiscount | Accounts | Student fee discounts/concessions |
| PeriodDefinition | Timetable | Period structure per school |
| TimetableEntry | Timetable | Subject/teacher assignment per period |
| TransferCertificate | Student | TC records |
| Notification | Common | Notification events (future) |

### Enum Changes
| Enum | Changes |
|------|---------|
| ExamType | Add: FA1, FA2, SA1, SA2 |
| FeeType | Add: LAB, LIBRARY, SPORTS, ANNUAL, DEVELOPMENT, LATE_FEE_PENALTY, UNIFORM, BOOKS |
| PaymentMode | Add: CHEQUE, DEMAND_DRAFT, ONLINE |
| DiscountType | New: SIBLING, MERIT_SCHOLARSHIP, RTE_QUOTA, STAFF_CHILDREN, CUSTOM |
| StudentStatus | Add: TRANSFERRED |

### New API Endpoints Required
| Method | Path | Module | Purpose |
|--------|------|--------|---------|
| GET | `/api/exams/{examId}/results` | Exam | Get results for an exam |
| GET | `/api/exams/{examId}/schedules` | Exam | List schedules for an exam |
| GET | `/api/exams/{examId}/report-card/{studentId}` | Exam | Generate PDF report card |
| DELETE | `/api/v1/accounts/fee-structures/{id}` | Accounts | Delete fee structure |
| PUT | `/api/v1/accounts/fee-structures/{id}` | Accounts | Update fee structure |
| PATCH | `/api/v1/accounts/fee-structures/{id}/toggle` | Accounts | Toggle fee structure active |
| GET | `/api/v1/accounts/student-fees/class/{classId}` | Accounts | Student fees by class |
| PATCH | `/academic-years/{id}/activate` | Academic Year | Activate academic year |
| POST | `/api/v1/accounts/fee-discounts` | Accounts | Create fee discount |
| GET | `/api/v1/accounts/fee-discounts/student/{studentId}` | Accounts | Get student discounts |
| POST | `/api/promotions/bulk` | Student | Bulk promote students |
| POST | `/students/{id}/transfer-certificate` | Student | Generate TC |
| GET | `/students/{id}/transfer-certificate` | Student | Download TC PDF |
| CRUD | `/api/timetable/periods` | Timetable | Period definitions |
| CRUD | `/api/timetable/entries` | Timetable | Timetable entries |
| GET | `/api/timetable/class/{classId}/section/{section}` | Timetable | Class timetable |
| GET | `/api/timetable/teacher/{teacherId}` | Timetable | Teacher timetable |
| GET | `/school/grading-scales` | School | Get grading scales |
| POST | `/school/grading-scales` | School | Create grading scale |

### New Frontend Pages/Routes
| Route | Page | Role |
|-------|------|------|
| `/admin/exams` | Exams | ADMIN |
| `/admin/attendance` | Attendance | ADMIN/TEACHER |
| `/admin/timetable` | Timetable | ADMIN |
| `/student` | StudentDashboard | STUDENT |
| `/student/attendance` | StudentAttendance | STUDENT |
| `/student/fees` | StudentFees | STUDENT |
| `/student/results` | StudentResults | STUDENT |

---

## Extension Configuration

| Extension | Enabled | Decided At |
|-----------|---------|------------|
| Security Baseline | Yes | Requirements Analysis |
| Property-Based Testing | No (school-based testing instead) | Requirements Analysis |

---

## Summary

This is a complex, system-wide enhancement of an existing brownfield School ERP. The scope includes:
- 3 new UI modules (Exams, Attendance, Student Portal)
- 3 new backend modules (Timetable, Promotion, TC)
- Enhancement of 2 existing modules (Fees with discounts + new types, Grading with multi-board support)
- ~15 new API endpoints
- ~7 new database entities
- 5 enum extensions
- 7 new frontend pages
- Bug fixes across 6 existing files
- Security rules enforced throughout
