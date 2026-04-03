# System Architecture

## System Overview

StudyMate is a monolithic Spring Boot application with a React SPA frontend. The backend uses a polyglot persistence strategy: PostgreSQL for relational data (school, students, teachers, fees, exams, attendance), MongoDB for auth/user data, and Redis for rate limiting and token blacklisting.

## Architecture Diagram

```
+--------------------------------------------------+
|                  React SPA (Vite)                 |
|  Port 5173 | Tailwind CSS | React Router | Axios |
+--------------------------------------------------+
                        |
                   HTTP (REST)
                        |
+--------------------------------------------------+
|            Spring Boot 3.3.5 (Java 21)           |
|                   Port 8080                       |
|  +----------------------------------------------+|
|  |  Security Layer (JWT + RBAC)                  ||
|  |  JwtAuthFilter -> SecurityConfig              ||
|  +----------------------------------------------+|
|  |  Controllers (REST API)                       ||
|  |  Auth | School | AcademicYear | Class |       ||
|  |  Section | Subject | ClassSubject |           ||
|  |  Student | Enrollment | Teacher |             ||
|  |  TeacherAssignment | Accounts |               ||
|  |  Exam | Attendance | TransportFee | Dashboard ||
|  +----------------------------------------------+|
|  |  Services (Business Logic)                    ||
|  +----------------------------------------------+|
|  |  Repositories (Data Access)                   ||
|  +----------------------------------------------+|
+--------+-------------+--------------+-----------+
         |             |              |
    +----+----+  +-----+-----+  +----+----+
    |PostgreSQL|  |  MongoDB  |  |  Redis  |
    | (Neon)   |  | (Atlas)   |  | (Docker)|
    |          |  |           |  |         |
    | school   |  | users     |  | rate    |
    | student  |  | (auth)    |  | limits  |
    | teacher  |  |           |  | token   |
    | class    |  |           |  | blacklist|
    | section  |  |           |  |         |
    | subject  |  |           |  |         |
    | enrollment|  |          |  |         |
    | fees     |  |           |  |         |
    | payments |  |           |  |         |
    | exams    |  |           |  |         |
    | attendance|  |          |  |         |
    +----------+  +-----------+  +---------+
```

## Multi-Tenancy Model

- School context is extracted from JWT token on every authenticated request
- A ThreadLocal-based `SchoolContext` holds the current school ID
- All repository queries are scoped to the current school
- No `schoolId` is passed in request DTOs — it's always derived from the authenticated user

## Data Flow — Login and Dashboard

```
User -> POST /auth/login (email, password)
     -> AuthService validates against MongoDB
     -> Returns JWT + refreshToken
     -> Frontend stores in localStorage

User -> GET /dashboard/summary
     -> JwtAuthFilter extracts schoolId from JWT
     -> SchoolContext.set(schoolId)
     -> DashboardService queries PostgreSQL (students, teachers, classes count)
     -> Returns DashboardSummaryResponse
```

## Integration Points

- **External APIs**: None currently (future: SMS/WhatsApp, payment gateway)
- **Databases**: PostgreSQL (Neon cloud), MongoDB Atlas, Redis (Docker/local)
- **Third-party Services**: None
- **Swagger UI**: Available at `/swagger-ui.html` for API exploration

## Security Architecture

- JWT-based stateless authentication
- BCrypt password encoding
- Role-based access: ADMIN, TEACHER, STUDENT
- Method-level security with `@PreAuthorize`
- Rate limiting on login and token refresh (Redis-backed)
- Token blacklisting on logout (Redis-backed)
- CORS configured for `http://localhost:5173`
