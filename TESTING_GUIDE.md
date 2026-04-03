# StudyMate ERP — Testing Guide

## Step 1: Seed MongoDB (Users)

Run this command to seed the admin and test users:

```bash
mongosh "$MONGODB_URI" seed-mongo.js
```

Or the app auto-seeds MongoDB users on startup (MongoDataSeeder.java).

## Step 2: Start Services

```bash
# Terminal 1: Start Redis
cd studymate/studymate
docker compose up redis -d

# Terminal 2: Start Backend (will run Flyway migrations + seed data)
cd studymate/studymate
./gradlew bootRun

# Terminal 3: Start Frontend
cd studymate-ui
npm run dev
```

## Step 3: Login & Test

Open http://localhost:5173

### Login Credentials
| Role | Email | Password |
|------|-------|----------|
| Admin | admin@sps.edu | Admin@123 |
| Teacher | priya.sharma@sps.edu | Admin@123 |
| Teacher | rajesh.kumar@sps.edu | Admin@123 |
| Student | student1@sps.edu | Admin@123 |

## Step 4: Test Each Module

### As Admin (admin@sps.edu)

1. **Dashboard** — Should show: 15 students, 5 teachers, 15 classes
2. **School Setup** → Academic Year tab — Should show 2024-2025 (ACTIVE)
3. **School Setup** → Classes tab — Should show Nursery through Class 12
4. **School Setup** → Subjects tab — Should show 7 subjects
5. **School Setup** → Grading Scale tab — Should show CBSE scale (A1-E)
6. **Students** → Student Directory — Should show 15 students
7. **Teachers** → Teacher Directory — Should show 5 teachers
8. **Staff** — Should show 4 staff members
9. **Timetable** → Period Setup — Should show 9 periods (7 class + 2 breaks)
10. **Accounts** → Fee Plans tab — Should show 3 plans (Day Scholar, Transport, Hosteller)
11. **Exams** — Create a new exam (e.g., SA1 2025)
12. **Attendance** — Mark attendance for a class

### As Teacher (priya.sharma@sps.edu)
Navigate to /teacher after login
1. **Dashboard** — Self-attendance button
2. **Marks Entry** — Select exam and enter marks
3. **Attendance** — Mark class attendance

### As Student (student1@sps.edu)
Navigate to /student after login
1. **Dashboard** — Overview cards
2. **Attendance** — View own attendance
3. **Fees** — View fee status
4. **Results** — View published results

## Seeded Data Summary

| Entity | Count | Details |
|--------|-------|---------|
| School | 1 | Sunrise Public School, CBSE, Delhi |
| Academic Years | 2 | 2024-2025 (active), 2023-2024 (completed) |
| Classes | 15 | Nursery, LKG, UKG, Class 1-12 |
| Subjects | 7 | Math, Science, English, Hindi, SST, CS, PE |
| Teachers | 5 | Priya, Rajesh, Anita, Suresh, Meena |
| Students | 15 | Indian names, Delhi addresses |
| Staff | 4 | Clerk, Librarian, Peon, Accountant |
| Grading Scale | 1 | CBSE (A1-E, 8 grades) |
| Fee Plans | 3 | Day Scholar, Transport, Hosteller |
| Period Definitions | 9 | 7 class periods + 2 breaks |
| MongoDB Users | 4 | 1 admin, 2 teachers, 1 student |
