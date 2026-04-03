# API Documentation

## Base URL: `http://localhost:8080`

## Authentication APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/auth/login` | Public | Login with email/password |
| POST | `/auth/logout` | Bearer | Logout (blacklist tokens) |
| POST | `/auth/refresh-token` | Public | Refresh JWT token |
| POST | `/auth/change-password` | Bearer | Change password |
| POST | `/auth/forgot-password` | Public | Request password reset |
| POST | `/auth/reset-password` | Public | Reset password with token |
| POST | `/admin/users` | ADMIN | Create user (teacher/student) |

## School APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/school` | Bearer | Get current school |
| POST | `/school` | Bearer | Create school (onboarding) |
| PUT | `/school` | Bearer | Update school |
| GET | `/dashboard/summary` | Bearer | Dashboard stats |

## Academic Year APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/academic-years` | Bearer | Create academic year |
| GET | `/academic-years` | Bearer | List all academic years |
| GET | `/academic-years/active` | Bearer | Get active academic year |

## Class Management APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/classes` | Bearer | Create class |
| GET | `/classes` | Bearer | List all classes |
| PUT | `/classes/{classId}` | Bearer | Update class |
| POST | `/classes/{classId}/sections` | Bearer | Create section |
| GET | `/classes/{classId}/sections` | Bearer | List sections for class |
| PUT | `/sections/{sectionId}` | Bearer | Update section |

## Subject APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/subjects` | Bearer | Create subject |
| GET | `/subjects` | Bearer | List all subjects |
| PUT | `/subjects/{subjectId}` | Bearer | Update subject |
| POST | `/class-subjects` | Bearer | Assign subjects to class |
| GET | `/class-subjects/classes/{classId}` | Bearer | Get subjects for class |

## Student APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/students` | Bearer | Create student |
| GET | `/students` | Bearer | List all students |
| PUT | `/students/{id}` | Bearer | Update student |
| POST | `/enrollments` | Bearer | Enroll student in class/section |
| GET | `/enrollments` | Bearer | List active year enrollments |

## Teacher APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/teachers` | Bearer | Create teacher |
| GET | `/teachers` | Bearer | List all teachers |
| PUT | `/teachers/{teacherId}` | Bearer | Update teacher |
| POST | `/teacher-assignments` | Bearer | Assign teacher to section/subject |
| GET | `/teacher-assignments/sections/{sectionId}` | Bearer | Assignments for section |
| GET | `/teacher-assignments/me` | Bearer | My assignments (teacher) |

## Accounts/Fees APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/v1/accounts/fee-structures` | ADMIN/PRINCIPAL/ACCOUNTANT | Create fee structure |
| GET | `/api/v1/accounts/fee-structures` | ADMIN/PRINCIPAL/ACCOUNTANT/TEACHER | List fee structures |
| POST | `/api/v1/accounts/student-fees/generate/class` | ADMIN/PRINCIPAL/ACCOUNTANT | Generate fees for class |
| GET | `/api/v1/accounts/student-fees/student/{studentId}` | Any authenticated | Get student fees |
| POST | `/api/v1/accounts/payments` | ADMIN/PRINCIPAL/ACCOUNTANT | Record payment |
| GET | `/api/v1/accounts/payments/receipt/{receiptNumber}` | ADMIN/PRINCIPAL/ACCOUNTANT/TEACHER/PARENT | Get payment by receipt |
| GET | `/api/v1/accounts/dashboard` | ADMIN/PRINCIPAL/ACCOUNTANT | Accounts dashboard |
| GET | `/api/v1/accounts/reports/daily-collection` | ADMIN/PRINCIPAL/ACCOUNTANT | Daily collection report |
| GET | `/api/v1/accounts/reports/outstanding-fees` | ADMIN/PRINCIPAL/ACCOUNTANT | Outstanding fees report |
| GET | `/api/v1/accounts/analytics/collection-trend` | ADMIN/PRINCIPAL/ACCOUNTANT | Collection trend |

## Exam APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/exams` | ADMIN | Create exam |
| POST | `/api/exams/schedule` | ADMIN | Schedule exam for class/section/subject |
| POST | `/api/exams/marks` | TEACHER/ADMIN | Enter student marks |
| POST | `/api/exams/{examId}/publish` | ADMIN | Publish exam results |
| GET | `/api/exams` | ADMIN/TEACHER | List all exams |

## Attendance APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/attendance/mark` | TEACHER/ADMIN | Mark attendance for section |
| GET | `/api/attendance/date/{date}/section/{section}` | TEACHER/ADMIN | Get attendance by date/section |
| GET | `/api/attendance/student/{studentId}/summary` | TEACHER/ADMIN/STUDENT | Monthly attendance summary |
| GET | `/api/attendance/student/{studentId}/history` | TEACHER/ADMIN/STUDENT | Attendance history |

## Transport Fee APIs

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/transport-fee-estimation` | ADMIN | Create transport fee estimation |
| GET | `/api/transport-fee-estimation` | ADMIN/TEACHER | List all estimations |
| GET | `/api/transport-fee-estimation/distance-slab/{slab}` | ADMIN/TEACHER | Get by distance slab |

## API Path Inconsistency Note

The API paths are inconsistent across modules:
- Core modules use root paths: `/school`, `/classes`, `/students`, `/teachers`, `/academic-years`
- Accounts uses versioned path: `/api/v1/accounts/...`
- Exam uses `/api/exams`
- Attendance uses `/api/attendance`
- Transport uses `/api/transport-fee-estimation`

This should be standardized in a future iteration.
