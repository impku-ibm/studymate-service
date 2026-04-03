# Technology Stack

## Programming Languages
- Java 21 — Backend application code
- JavaScript (JSX) — Frontend React components
- SQL — Database migrations (Flyway)

## Backend Frameworks
- Spring Boot 3.3.5 — Application framework
- Spring Security — Authentication and authorization
- Spring Data JPA — PostgreSQL ORM
- Spring Data MongoDB — MongoDB access
- Spring Data Redis — Redis access
- Spring Validation — Input validation (Jakarta Bean Validation)
- Flyway 10.15.0 — Database migration management
- Lombok — Boilerplate reduction (getters, setters, builders)
- JJWT 0.11.5 — JWT token creation and validation
- SpringDoc OpenAPI 2.5.0 — Swagger UI and API documentation

## Frontend Frameworks
- React 19.2.0 — UI framework
- React Router DOM 7.11.0 — Client-side routing
- Vite 7.2.4 — Build tool and dev server
- Tailwind CSS 4.1.18 — Utility-first CSS framework
- Axios 1.13.2 — HTTP client
- Recharts 3.6.0 — Charting library

## Databases
- PostgreSQL (Neon cloud) — Relational data (school, students, teachers, fees, exams, attendance)
- MongoDB Atlas — Auth/user data (users collection)
- Redis 7 (Docker Alpine) — Rate limiting, token blacklisting

## Infrastructure
- Docker — Containerization (Dockerfile + docker-compose.yml)
- Neon — Managed PostgreSQL (cloud)
- MongoDB Atlas — Managed MongoDB (cloud)

## Build Tools
- Gradle 8.x (Groovy DSL) — Backend build
- npm — Frontend package management
- Vite — Frontend bundling

## Testing Tools
- JUnit 5 (JUnit Platform) — Backend unit testing
- Spring Boot Test — Integration testing
- Spring Security Test — Security testing
- ESLint 9.x — Frontend linting

## Dev Tools
- SpringDoc Swagger UI — API exploration at `/swagger-ui.html`
- Spring Boot Actuator — Health checks at `/actuator/health`
