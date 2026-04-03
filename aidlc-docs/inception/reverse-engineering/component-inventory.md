# Component Inventory

## Backend Modules (Application Packages)

| # | Module | Controllers | Services | Entities | DTOs | Enums |
|---|--------|------------|----------|----------|------|-------|
| 1 | auth | 2 | 2 | 1 (MongoDB) | 8 | - |
| 2 | school | 2 | 2 | 1 | 4 | - |
| 3 | academicyear | 1 | 1 | 1 | 2 | - |
| 4 | classmanagement | 2 | 2 | 2 | 6 | - |
| 5 | subject | 1 | 1 | 1 | 3 | - |
| 6 | classsubject | 1 | 1 | 1 | 2 | - |
| 7 | student | 2 | 2 | 2 | 5 | 2 |
| 8 | teachermgmt | 1 | 1 | 1 | 3 | - |
| 9 | teacherassignment | 1 | 1 | 1 | 2 | - |
| 10 | accounts | 2 | 5 | 4 | 10 | 2 |
| 11 | exam | 1 | 1 | 4 | 5 | 3 |
| 12 | attendance | 1 | 1 | 2 | 3 | 1 |
| **Total** | **12** | **17** | **20** | **21** | **53** | **8** |

## Infrastructure Packages
- `config/` — OpenApiConfig, MethodSecurityConfig, RedisConfiguration
- `common/jwt/` — JwtUtil, JwtAuthFilter
- `common/context/` — SchoolContext (ThreadLocal)
- `common/exception/` — Global exception handlers
- `common/util/` — Role enum
- `common/dto/` — Common DTOs

## Frontend Components

| # | Category | Component | Status |
|---|----------|-----------|--------|
| 1 | Layout | DashboardLayout | Built |
| 2 | Layout | Sidebar | Built |
| 3 | Layout | Topbar | Built |
| 4 | Layout | ProtectedRoute | Built |
| 5 | Page | Landing | Built |
| 6 | Page | Login | Built |
| 7 | Page | AdminDashboard | Built |
| 8 | Page | SchoolOnboarding | Built (buggy) |
| 9 | Page | SchoolSetup | Built |
| 10 | Page | Students | Built |
| 11 | Page | Teachers | Built |
| 12 | Page | Account | Built |
| 13 | Page | Exams | NOT BUILT |
| 14 | Page | Attendance | NOT BUILT |
| 15 | Setup | AcademicYearSetup | Built |
| 16 | Setup | ClassSetup | Built |
| 17 | Setup | SectionSetup | Built |
| 18 | Setup | SubjectSetup | Built |
| 19 | Setup | ClassSubjectSetup | Built (not wired to tabs) |
| 20 | Students | StudentDirectory | Built |
| 21 | Students | StudentEnrollment | Built |
| 22 | Students | AddStudentModal | Built |
| 23 | Students | EnrollStudentModal | Built |
| 24 | Teachers | TeacherDirectory | Built |
| 25 | Teachers | TeacherAssignments | Built |
| 26 | Teachers | AddTeacherModal | Built |
| 27 | Teachers | AssignTeacherModal | Built |
| 28 | Accounts | FeeStructure | Built |
| 29 | Accounts | FeeCollection | Built (wrong API path) |
| 30 | Accounts | FeeReports | Built |
| 31 | Accounts | DefineFeeModal | Built |
| 32 | Accounts | RecordPaymentModal | Built |
| 33 | Reports | Dashboard | Built |
| 34 | Reports | DailyCollectionReport | Built |
| 35 | Reports | OutstandingFeesReport | Built |
| 36 | Reports | CollectionTrendChart | Built |
| 37 | Common | Modal | Built |
| 38 | Exams | (all components) | NOT BUILT |
| 39 | Attendance | (all components) | NOT BUILT |

## Database Tables (PostgreSQL)

| # | Table | Module | Migration |
|---|-------|--------|-----------|
| 1 | school | School | V1 |
| 2 | academic_year | Academic Year | V1 |
| 3 | school_class | Class Mgmt | V1 |
| 4 | section | Class Mgmt | V1 |
| 5 | subject | Subject | V1 |
| 6 | student | Student | V2 |
| 7 | student_enrollment | Student | V2 |
| 8 | teacher | Teacher | V3 |
| 9 | teacher_assignment | Teacher | V3 |
| 10 | class_subject | Class-Subject | V4 |
| 11 | fee_structure | Accounts | V6 |
| 12 | student_fee | Accounts | V6 |
| 13 | payment | Accounts | V6 |
| 14 | payment_detail | Accounts | V6 |
| 15 | exams | Exam | V20241201_001 |
| 16 | exam_schedules | Exam | V20241201_001 |
| 17 | exam_marks | Exam | V20241201_001 |
| 18 | student_results | Exam | V20241201_001 |
| 19 | student_attendance | Attendance | V20241201_001 |
| 20 | teacher_attendance | Attendance | V20241201_001 |
| 21 | transport_fee_estimation | Accounts | V20241201_002 |

## MongoDB Collections
| # | Collection | Module |
|---|-----------|--------|
| 1 | users | Auth |

## Total Counts
- **Backend Modules**: 12
- **Controllers**: 17
- **API Endpoints**: ~50
- **JPA Entities**: 20
- **MongoDB Documents**: 1
- **Database Tables**: 21
- **Frontend Pages**: 8 built, 2 missing
- **Frontend Components**: 37 built, ~10 missing
