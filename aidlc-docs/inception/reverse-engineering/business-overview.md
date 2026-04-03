# Business Overview

## Business Context

StudyMate is a multi-tenant School ERP (Enterprise Resource Planning) system designed for Indian schools. It digitizes core school operations — student management, teacher management, academic scheduling, attendance tracking, examination management, and fee collection.

## Business Description

- **Business Description**: A cloud-hosted SaaS platform where each school is an isolated tenant. An ADMIN user sets up the school, configures academic years, classes, sections, and subjects, then manages students, teachers, exams, attendance, and finances through a unified dashboard.
- **Target Market**: Indian K-12 schools (CBSE, ICSE, State Board)
- **Deployment Model**: Single backend serving multiple schools, each isolated by school context extracted from JWT

## Business Transactions

| # | Transaction | Description |
|---|------------|-------------|
| 1 | School Onboarding | Admin creates school profile (name, board, code, address, academic start month) |
| 2 | Academic Year Setup | Admin creates academic years (e.g., 2024-2025) and activates one |
| 3 | Class & Section Setup | Admin defines classes (Class 1–12) and sections (A, B, C) per class |
| 4 | Subject Setup | Admin creates subjects and assigns them to classes |
| 5 | Student Registration | Admin registers students with admission number, parent details, address |
| 6 | Student Enrollment | Admin enrolls students into a class/section with roll number for the active academic year |
| 7 | Teacher Registration | Admin creates teacher profiles (auto-creates auth user) |
| 8 | Teacher Assignment | Admin assigns teachers to sections for specific subjects |
| 9 | Fee Structure Definition | Admin defines fee types (Tuition, Exam, Transport, etc.) per class per academic year |
| 10 | Fee Generation | System generates student-level fees from fee structure (bulk per class) |
| 11 | Payment Recording | Admin/Accountant records payments against student fees (Cash, UPI, Card, Bank Transfer) |
| 12 | Exam Creation | Admin creates exams (Mid-Term, Final, etc.) per academic year |
| 13 | Exam Scheduling | Admin schedules exams per class/section/subject with max marks, pass marks, duration |
| 14 | Marks Entry | Teacher enters marks for students per scheduled exam |
| 15 | Result Publishing | Admin publishes exam results (generates grades, ranks, pass/fail) |
| 16 | Attendance Marking | Teacher marks daily attendance per section (Present/Absent/Leave) |
| 17 | Attendance Reporting | View student attendance summary (monthly %) and history |
| 18 | Transport Fee Estimation | Admin configures distance-based transport fee slabs |
| 19 | Dashboard Overview | Admin views aggregate stats (total students, teachers, classes, active year) |
| 20 | Authentication | Login with email/password, JWT tokens, refresh tokens, password management |

## Business Dictionary

| Term | Meaning |
|------|---------|
| School | A tenant in the system — all data is scoped to a school |
| Academic Year | A fiscal academic period (e.g., April 2024 – March 2025) |
| Class | An academic grade level (e.g., Class 5, Class 10) |
| Section | A division within a class (e.g., Section A, Section B) |
| Enrollment | The act of assigning a student to a class/section for an academic year |
| Admission Number | A unique identifier for a student within a school |
| Roll Number | A student's number within their enrolled class/section |
| Fee Structure | A template defining fee type, amount, and due date per class per year |
| Student Fee | An individual fee record generated for a specific student from the fee structure |
| Payment | A financial transaction recording money received against one or more student fees |
| Exam | A named examination event (Mid-Term, Final) for an academic year |
| Exam Schedule | A specific subject exam within an exam, for a class/section, with date and marks config |
| Exam Marks | Individual student marks for a scheduled exam |
| Student Result | Aggregated result for a student across all subjects in an exam (percentage, grade, rank) |

## Component Level Business Descriptions

### Auth Module
- **Purpose**: User authentication and authorization
- **Responsibilities**: Login, logout, JWT token management, refresh tokens, password reset, rate limiting, user creation

### School Module
- **Purpose**: School profile and dashboard management
- **Responsibilities**: School CRUD, dashboard summary aggregation

### Academic Year Module
- **Purpose**: Academic year lifecycle management
- **Responsibilities**: Create/list academic years, activate/deactivate years

### Class Management Module
- **Purpose**: Academic structure definition
- **Responsibilities**: Class CRUD, section CRUD within classes

### Subject Module
- **Purpose**: Subject catalog management
- **Responsibilities**: Subject CRUD, class-subject assignment

### Student Module
- **Purpose**: Student lifecycle management
- **Responsibilities**: Student registration, enrollment into classes/sections

### Teacher Management Module
- **Purpose**: Teacher profile management
- **Responsibilities**: Teacher CRUD, auto-creates auth user on teacher creation

### Teacher Assignment Module
- **Purpose**: Teacher-to-class/subject mapping
- **Responsibilities**: Assign teachers to sections for subjects, view assignments

### Accounts Module
- **Purpose**: Financial management
- **Responsibilities**: Fee structure definition, student fee generation, payment recording, financial reports, transport fee estimation

### Exam Module
- **Purpose**: Examination lifecycle management
- **Responsibilities**: Exam creation, scheduling, marks entry, result generation and publishing

### Attendance Module
- **Purpose**: Daily attendance tracking
- **Responsibilities**: Mark student/teacher attendance, view summaries and history
