# Dependencies

## Internal Module Dependencies

```
Auth Module
  +-- MongoDB (users collection)
  +-- Redis (rate limiting, token blacklist)
  +-- common/jwt (JwtUtil, JwtAuthFilter)
  +-- common/util (Role enum)

School Module
  +-- Auth (school context from JWT)
  +-- Student Module (count for dashboard)
  +-- Teacher Module (count for dashboard)
  +-- Class Module (count for dashboard)
  +-- Academic Year Module (active year for dashboard)

Academic Year Module
  +-- School Module (school scoping)

Class Management Module
  +-- School Module (school scoping)

Subject Module
  +-- School Module (school scoping)

Class-Subject Module
  +-- Class Management Module (SchoolClass)
  +-- Subject Module (Subject)

Student Module
  +-- School Module (school scoping)
  +-- Auth Module (user creation on student registration)

Student Enrollment Module
  +-- Student Module (Student)
  +-- Class Management Module (SchoolClass, Section)
  +-- Academic Year Module (active year)

Teacher Management Module
  +-- School Module (school scoping)
  +-- Auth Module (user creation on teacher registration)

Teacher Assignment Module
  +-- Teacher Module (Teacher)
  +-- Class Management Module (Section)
  +-- Subject Module (Subject)

Accounts Module
  +-- School Module (school scoping)
  +-- Academic Year Module (fee structure per year)
  +-- Class Management Module (fee structure per class)
  +-- Student Module (student fees, payments)
  +-- Exam Module (event listener for exam fee generation)

Exam Module
  +-- School Module (school scoping)
  +-- Academic Year Module (exam per year)
  +-- Class Management Module (exam schedule per class)
  +-- Subject Module (exam schedule per subject)
  +-- Student Module (marks per student)
  +-- Teacher Module (marks entered by teacher)

Attendance Module
  +-- Student Module (student attendance)
  +-- Teacher Module (teacher attendance, marked by)
  +-- Academic Year Module (attendance per year)
```

## External Dependencies (Backend)

| Dependency | Version | Purpose |
|-----------|---------|---------|
| spring-boot-starter-web | 3.3.5 | REST API framework |
| spring-boot-starter-data-jpa | 3.3.5 | PostgreSQL ORM |
| spring-boot-starter-data-mongodb | 3.3.5 | MongoDB access |
| spring-boot-starter-data-redis | 3.3.5 | Redis access |
| spring-boot-starter-security | 3.3.5 | Authentication/authorization |
| spring-boot-starter-validation | 3.3.5 | Input validation |
| spring-boot-starter-actuator | 3.3.5 | Health checks |
| springdoc-openapi-starter-webmvc-ui | 2.5.0 | Swagger UI |
| flyway-core | 10.15.0 | DB migrations |
| flyway-database-postgresql | 10.15.0 | PostgreSQL Flyway support |
| postgresql | (runtime) | JDBC driver |
| lombok | (compile) | Boilerplate reduction |
| jjwt-api | 0.11.5 | JWT creation/validation |
| jjwt-impl | 0.11.5 | JWT implementation |
| jjwt-jackson | 0.11.5 | JWT JSON serialization |

## External Dependencies (Frontend)

| Dependency | Version | Purpose |
|-----------|---------|---------|
| react | 19.2.0 | UI framework |
| react-dom | 19.2.0 | DOM rendering |
| react-router-dom | 7.11.0 | Client-side routing |
| axios | 1.13.2 | HTTP client |
| recharts | 3.6.0 | Charts and graphs |
| tailwindcss | 4.1.18 | CSS framework |
| vite | 7.2.4 | Build tool |
| eslint | 9.39.1 | Linting |
