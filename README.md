# StudyMate ERP — Backend

Production-grade School ERP system for Indian schools (CBSE/ICSE/State Board).

## Tech Stack
- Java 21, Spring Boot 3.3.5
- PostgreSQL (Neon), MongoDB Atlas, Redis
- JWT Authentication, Role-based Access Control
- Flyway Database Migrations

## Modules
| Module | Endpoints | Description |
|--------|:---------:|-------------|
| Auth | 7 | Login, logout, JWT refresh, password management |
| School | 4 | School profile, dashboard summary |
| Academic Year | 4 | Year management, activate/deactivate |
| Classes | 3 | Class CRUD |
| Sections | 3 | Section CRUD per class |
| Subjects | 3 | Subject CRUD |
| Class-Subjects | 2 | Assign subjects to classes |
| Students | 3 | Student registration |
| Enrollments | 2 | Class enrollment |
| Teachers | 3 | Teacher profiles |
| Teacher Assignments | 3 | Teacher-subject-section mapping |
| Exams | 8 | Exam lifecycle, marks, results, grace marks |
| Attendance | 5 | Student + teacher attendance |
| Accounts | 10 | Fee structures, payments, reports |
| Fee Plans | 6 | Student category fee plans |
| Fee Discounts | 3 | Concessions (sibling, RTE, merit) |
| Grading | 3 | Configurable grading scales |
| Timetable | 7 | Period definitions, class/teacher timetables |
| Staff | 4 | Non-teaching staff management |
| Promotion | 1 | Bulk student promotion |
| Transfer Certificate | 2 | TC generation |

## Quick Start

### Prerequisites
- Java 21
- Docker (for Redis)
- PostgreSQL database (or Neon account)
- MongoDB database (or Atlas account)

### Setup
```bash
# 1. Clone
git clone <repo-url>
cd studymate-backend

# 2. Configure
cp .env.example .env
# Edit .env with your database credentials

# 3. Start Redis
docker compose up redis -d

# 4. Run (Flyway auto-migrates + seeds test data)
./gradlew bootRun
```

### Environment Variables
```
MONGODB_URI=mongodb+srv://...
SPRING_DATASOURCE_URL=jdbc:postgresql://...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
JWT_SECRET=your-secret-key-min-32-chars
REDIS_HOST=localhost
REDIS_PORT=6379
```

### Test Credentials (auto-seeded in local profile)
| Role | Email | Password |
|------|-------|----------|
| Admin | admin@sps.edu | Admin@123 |
| Teacher | priya.sharma@sps.edu | Admin@123 |
| Student | student1@sps.edu | Admin@123 |

## API Documentation
Swagger UI: http://localhost:8080/swagger-ui.html

## Running Tests
```bash
./gradlew test
# 136 tests, 0 failures
```

## Deployment (Render)
1. Create a Web Service on Render
2. Connect this GitHub repo
3. Build command: `./gradlew build -x test`
4. Start command: `java -jar build/libs/studymate-0.0.1-SNAPSHOT.jar`
5. Set environment variables in Render dashboard
