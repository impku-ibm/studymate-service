# Code Structure

## Backend (studymate/studymate/)

### Build System
- **Type**: Gradle (Groovy DSL)
- **Java**: 21 (toolchain)
- **Spring Boot**: 3.3.5
- **Configuration**: `build.gradle`, `settings.gradle`

### Module Structure (Controller -> Service -> Repository -> Model)

Each module follows a consistent layered pattern:

```
module-name/
  controller/   -- REST endpoints
  service/      -- Business logic
  repository/   -- Data access (JPA or MongoDB)
  model/        -- JPA entities or MongoDB documents
  dto/          -- Request/Response DTOs
  enums/        -- Module-specific enums (where applicable)
  exception/    -- Module-specific exceptions (where applicable)
```

### Backend File Inventory

#### Auth Module (MongoDB-backed)
- `auth/controller/AuthController.java` — Login, logout, refresh, password management
- `auth/controller/AdminUserController.java` — Admin user creation
- `auth/service/AuthService.java` — Auth business logic
- `auth/service/RateLimitService.java` — Redis-backed rate limiting
- `auth/model/User.java` — MongoDB document (users collection)
- `auth/dtos/LoginRequest.java`, `LoginResponse.java`, `SignupRequest.java`, `ChangePasswordRequest.java`, `ForgotPasswordRequest.java`, `ResetPasswordRequest.java`, `RefreshTokenRequest.java`, `LogoutRequest.java`
- `auth/security/SecurityConfig.java` — Spring Security config, CORS, JWT filter
- `auth/exception/InvalidTokenException.java`

#### School Module
- `school/controller/SchoolController.java` — School CRUD
- `school/controller/DashboardController.java` — Dashboard summary
- `school/service/SchoolService.java`, `DashboardService.java`
- `school/model/School.java` — JPA entity
- `school/dtos/CreateSchoolRequest.java`, `SchoolResponse.java`, `UpdateSchoolRequest.java`, `DashboardSummaryResponse.java`

#### Academic Year Module
- `academicyear/controller/AcademicYearController.java`
- `academicyear/service/AcademicYearService.java`
- `academicyear/model/AcademicYear.java`
- `academicyear/dto/CreateAcademicYearRequest.java`, `AcademicYearResponse.java`

#### Class Management Module
- `classmanagement/controller/ClassController.java`, `SectionController.java`
- `classmanagement/service/ClassService.java`, `SectionService.java`
- `classmanagement/model/SchoolClass.java`, `Section.java`
- `classmanagement/dto/CreateClassRequest.java`, `ClassResponse.java`, `UpdateClassRequest.java`, `CreateSectionRequest.java`, `SectionResponse.java`, `UpdateSectionRequest.java`

#### Subject Module
- `subject/controller/SubjectController.java`
- `subject/service/SubjectService.java`
- `subject/model/Subject.java`
- `subject/dto/CreateSubjectRequest.java`, `SubjectResponse.java`, `UpdateSubjectRequest.java`

#### Class-Subject Module
- `classsubject/controller/ClassSubjectController.java`
- `classsubject/service/ClassSubjectService.java`
- `classsubject/model/ClassSubject.java`
- `classsubject/dto/AssignSubjectsRequest.java`, `ClassSubjectResponse.java`

#### Student Module
- `student/controller/StudentController.java`, `StudentEnrollmentController.java`
- `student/service/StudentService.java`, `StudentEnrollmentService.java`
- `student/model/Student.java`, `StudentEnrollment.java`, `StudentStatus.java`, `EnrollmentStatus.java`
- `student/dto/CreateStudentRequest.java`, `StudentResponse.java`, `UpdateStudentRequest.java`, `EnrollStudentRequest.java`, `StudentEnrollmentResponse.java`

#### Teacher Management Module
- `teachermgmt/controller/TeacherController.java`
- `teachermgmt/service/TeacherService.java`
- `teachermgmt/model/Teacher.java`
- `teachermgmt/dto/CreateTeacherRequest.java`, `TeacherResponse.java`, `UpdateTeacherRequest.java`

#### Teacher Assignment Module
- `teacherassignment/controller/TeacherAssignmentController.java`
- `teacherassignment/service/TeacherAssignmentService.java`
- `teacherassignment/model/TeacherAssignment.java`
- `teacherassignment/dto/CreateTeacherAssignmentRequest.java`, `TeacherAssignmentResponse.java`

#### Accounts Module
- `accounts/controller/AccountsController.java` — Fee structures, student fees, payments, reports
- `accounts/controller/TransportFeeController.java` — Transport fee estimation
- `accounts/service/FeeStructureService.java`, `StudentFeeService.java`, `PaymentService.java`, `ReportsService.java`, `TransportFeeEstimationService.java`
- `accounts/model/FeeStructure.java`, `Payment.java`, `StudentFee.java`, `TransportFeeEstimation.java`
- `accounts/dtos/requests/CreateFeeStructureRequest.java`, `RecordPaymentRequest.java`, `CreateTransportEstimationRequest.java`
- `accounts/dtos/responses/FeeStructureResponse.java`, `PaymentResponse.java`, `StudentFeeResponse.java`, `AccountsDashboardResponse.java`, `DailyCollectionReportResponse.java`, `OutstandingFeesReportResponse.java`, `FeeCollectionTrendResponse.java`, `TransportFeeEstimationResponse.java`
- `accounts/enums/FeeType.java` (TUITION, ADMISSION, EXAM, TRANSPORT, HOSTEL, MISC)
- `accounts/enums/PaymentMode.java` (CASH, UPI, CARD, BANK_TRANSFER)
- `accounts/component/FeeGenerationListener.java` — Event-driven fee generation

#### Exam Module
- `exam/controller/ExamController.java` — 5 endpoints
- `exam/service/ExamService.java`
- `exam/model/Exam.java`, `ExamSchedule.java`, `ExamMarks.java`, `StudentResult.java`
- `exam/dto/CreateExamRequest.java`, `CreateExamScheduleRequest.java`, `EnterMarksRequest.java`, `ExamResponse.java`, `ExamCreatedEvent.java`
- `exam/enums/ExamType.java`, `ExamStatus.java`, `ResultStatus.java`
- `exam/repository/ExamRepository.java`, `ExamScheduleRepository.java`, `ExamMarksRepository.java`, `StudentResultRepository.java`

#### Attendance Module
- `attendance/controller/AttendanceController.java` — 4 endpoints
- `attendance/service/AttendanceService.java`
- `attendance/model/StudentAttendance.java`, `TeacherAttendance.java`
- `attendance/dto/MarkAttendanceRequest.java`, `AttendanceResponse.java`, `AttendanceSummary.java`
- `attendance/enums/AttendanceStatus.java` (PRESENT, ABSENT, LEAVE)
- `attendance/repository/StudentAttendanceRepository.java`

#### Common/Config
- `common/jwt/JwtUtil.java`, `JwtAuthFilter.java`
- `common/context/SchoolContext.java` — ThreadLocal school context
- `common/util/Role.java` (ADMIN, TEACHER, STUDENT)
- `common/exception/` — Global exception handling
- `config/OpenApiConfig.java`, `MethodSecurityConfig.java`, `RedisConfiguration.java`

### Database Migrations (Flyway)
- `V1__init_school_erp.sql` — school, academic_year, school_class, section, subject tables
- `V2__create_student_and_enrollment_tables.sql` — student, student_enrollment tables
- `V3__create_teacher_and_teacher_assignment_tables.sql` — teacher, teacher_assignment tables
- `V4__create_class_subject_table.sql` — class_subject table
- `V5__fix_student_enrollment_school_class_column.sql` — Column fix
- `V6__Create_Accounts_Tables.sql` — fee_structure, student_fee, payment, payment_detail tables
- `V8__Create_Student_Fee_Configuration_Table.sql` — Student fee config
- `V20241201_001__create_exam_tables.sql` — exams, exam_schedules, exam_marks, student_results, student_attendance, teacher_attendance
- `V20241201_002__create_transport_fee_estimation.sql` — transport_fee_estimation

---

## Frontend (studymate-ui/)

### Build System
- **Bundler**: Vite 7.2.4
- **Framework**: React 19.2.0
- **Styling**: Tailwind CSS 4.1.18
- **Routing**: React Router DOM 7.11.0
- **HTTP Client**: Axios 1.13.2
- **Charts**: Recharts 3.6.0

### Frontend File Inventory

#### API Layer
- `src/api/axios.js` — Base Axios instance, JWT interceptor, 401 redirect
- `src/api/accountsApi.js` — Accounts-specific API functions

#### Context
- `src/context/AcademicYearContext.jsx` — Global academic year state

#### Layout
- `src/layout/DashboardLayout.jsx` — Sidebar + Topbar + Outlet
- `src/layout/PageContainer.jsx` — Page wrapper

#### Pages
- `src/pages/Landing.jsx` — Public landing page
- `src/pages/Login.jsx` — Login form
- `src/pages/AdminDashboard.jsx` — Dashboard with stat cards
- `src/pages/SchoolOnboarding.jsx` — One-time school setup (BUGGY)
- `src/pages/SchoolSetup.jsx` — Tabs: Academic Year, Classes, Sections, Subjects
- `src/pages/Students.jsx` — Tabs: Student Directory, Class Enrollment
- `src/pages/Teachers.jsx` — Tabs: Teacher Directory, Teaching Assignments
- `src/pages/accounts/Account.jsx` — Tabs: Fee Structure, Fee Collection, Reports

#### Components
- `src/components/Sidebar.jsx` — Navigation sidebar
- `src/components/Topbar.jsx` — Top header with logout
- `src/components/ProtectedRoute.jsx` — Auth guard
- `src/components/common/Modal.jsx` — Reusable modal
- `src/components/setup/` — AcademicYearSetup, ClassSetup, SectionSetup, SubjectSetup + modals
- `src/components/students/` — StudentDirectory, StudentEnrollment, AddStudentModal, EnrollStudentModal
- `src/components/teachers/` — TeacherDirectory, TeacherAssignments, AddTeacherModal, AssignTeacherModal, TeacherAssignmentTable
- `src/components/accounts/` — FeeStructure, FeeCollection, FeeReports, DefineFeeModal, RecordPaymentModal
- `src/components/accounts/reports/` — Dashboard, DailyCollectionReport, OutstandingFeesReport, CollectionTrendChart

#### Utils
- `src/utils/auth.js` — Logout utility

### Design Patterns

#### Backend
- **Layered Architecture**: Controller -> Service -> Repository
- **DTO Pattern**: Separate request/response DTOs from entities
- **Multi-tenancy**: ThreadLocal SchoolContext from JWT
- **Event-Driven**: Spring Events for cross-module communication (fee generation on enrollment/exam creation)
- **Builder Pattern**: Lombok @Builder on DTOs and entities

#### Frontend
- **Component Composition**: Pages contain tab containers, tabs render feature components
- **Inline API Calls**: Most components call axios directly (no centralized API layer except accounts)
- **Context API**: AcademicYearContext for global state
- **Modal Pattern**: Add/Edit modals as separate components with onClose/onSuccess callbacks
