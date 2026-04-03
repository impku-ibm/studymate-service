# Requirements Verification Questions

Please answer each question by filling in the letter choice after the `[Answer]:` tag.
If none of the options match, choose the last option (Other) and describe your preference.

---

## Section 1: Indian School Context

## Question 1
Which education board(s) should the ERP support? This affects grading systems, exam patterns, and report card formats.

A) CBSE only (5-point grading: A1, A2, B1, B2, C1, C2, D, E)
B) ICSE only (percentage-based grading)
C) State Board only (varies by state)
D) Multi-board support (configurable grading per school)
E) Other (please describe after [Answer]: tag below)

[Answer]: D

## Question 2
What class range should the system support?

A) Pre-primary to Class 12 (Nursery, LKG, UKG, Class 1-12)
B) Class 1 to Class 12 only
C) Class 1 to Class 10 only
D) Configurable per school (admin defines class names)
E) Other (please describe after [Answer]: tag below)

[Answer]: D

## Question 3
What academic year cycle does the target school follow?

A) April to March (standard Indian academic year)
B) June to May (some state boards)
C) Configurable per school (already have `academicStartMonth` in School entity)
D) Other (please describe after [Answer]: tag below)

[Answer]: c

---

## Section 2: Module Priorities

## Question 4
What is the priority order for building the missing UI? (Rank 1 = build first)

A) Exams UI first, then Attendance UI, then bug fixes
B) Attendance UI first, then Exams UI, then bug fixes
C) Fix existing bugs first, then Exams UI, then Attendance UI
D) Build all three in parallel (Exams + Attendance + bug fixes together)
E) Other (please describe after [Answer]: tag below)

[Answer]: D

## Question 5
Should we build a Student/Parent portal (read-only view of attendance, results, fees)?

A) Yes, build it now as part of this iteration
B) Yes, but later — focus on Admin/Teacher views first
C) No, not needed for now
D) Other (please describe after [Answer]: tag below)

[Answer]: A

---

## Section 3: Exam Module Specifics

## Question 6
What exam structure should the system support?

A) Simple: One exam type per academic year (Mid-Term, Final only)
B) CBSE pattern: FA1, FA2, SA1, SA2 (Formative + Summative assessments)
C) Flexible: Admin defines exam types per school (current implementation with 7 types)
D) Term-based: Term 1 exams + Term 2 exams with unit tests
E) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 7
Should the system generate PDF report cards?

A) Yes, with school logo, student photo, subject-wise marks, grade, rank
B) Yes, basic text-based report card (no images)
C) No, just show results on screen for now
D) Other (please describe after [Answer]: tag below)

[Answer]: A

---

## Section 4: Fee Management Specifics

## Question 8
Which additional fee types do Indian schools in your target market need? (Current: TUITION, ADMISSION, EXAM, TRANSPORT, HOSTEL, MISC)

A) Add: LAB, LIBRARY, SPORTS, ANNUAL, DEVELOPMENT — that covers most schools
B) Add the above plus: LATE_FEE_PENALTY, UNIFORM, BOOKS
C) Keep current 6 types — they're enough
D) Make fee types fully configurable (admin creates custom fee types)
E) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 9
Should the system support fee concessions/discounts?

A) Yes — sibling discount, merit scholarship, RTE quota, staff children discount
B) Yes — but just a simple percentage discount per student
C) No, not needed for now
D) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 10
Should the system support online payment integration?

A) Yes, integrate with Razorpay (popular in Indian schools)
B) Yes, integrate with PayU or Cashfree
C) No, only offline payment recording (Cash, UPI, Card, Bank Transfer) — current implementation
D) Plan for it later, but design the payment flow to be extensible
E) Other (please describe after [Answer]: tag below)

[Answer]: E , it should be able to accept payment via card , cash and UPI or even other payment mode.

---

## Section 5: Attendance Specifics

## Question 11
What attendance model should the system use?

A) Daily attendance per section (one entry per student per day) — current backend design
B) Period-wise attendance (per subject per period)
C) Both daily and period-wise (configurable per school)
D) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 12
Should the system send attendance alerts to parents?

A) Yes, SMS alerts for absent students (integrate with SMS gateway)
B) Yes, WhatsApp alerts (integrate with WhatsApp Business API)
C) No, just show attendance in the system for now
D) Plan for it later, but design the notification system to be extensible
E) Other (please describe after [Answer]: tag below)

[Answer]: D

---

## Section 6: Additional Features

## Question 13
Which of these features should be included in this iteration?

A) Student promotion (bulk promote Class 5 → Class 6 at year end)
B) Transfer Certificate (TC) generation
C) Timetable management
D) All of the above
E) None — focus on Exams, Attendance, and bug fixes only
F) Other (please describe after [Answer]: tag below)

[Answer]: D

## Question 14
What about the existing bugs found during reverse engineering? Should we fix them now?

A) Yes, fix all critical bugs before building new features (SchoolOnboarding, FeeCollection wrong path, missing backend endpoints, Exams route)
B) Fix only the ones that block current functionality (FeeCollection, Exams route)
C) Fix them alongside new feature development
D) Other (please describe after [Answer]: tag below)

[Answer]: C

---

## Section 7: Non-Functional Requirements

## Question 15
What is the expected scale for the first deployment?

A) Single school, under 500 students
B) Single school, 500-2000 students
C) Multiple schools (SaaS), each under 1000 students
D) Multiple schools (SaaS), large scale (1000+ students per school)
E) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 16
What is the deployment target?

A) Run locally for now, deploy to cloud later
B) Deploy to AWS (EC2/ECS + RDS)
C) Deploy to a VPS (DigitalOcean, Hetzner, etc.)
D) Docker Compose on a single server
E) Other (please describe after [Answer]: tag below)

[Answer]: A

---

## Section 8: Extensions

## Question 17: Security Extensions
Should security extension rules be enforced for this project?

A) Yes — enforce all SECURITY rules as blocking constraints (recommended for production-grade applications)
B) No — skip all SECURITY rules (suitable for PoCs, prototypes, and experimental projects)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 18: Property-Based Testing Extension
Should property-based testing (PBT) rules be enforced for this project?

A) Yes — enforce all PBT rules as blocking constraints (recommended for projects with business logic, data transformations, serialization, or stateful components)
B) Partial — enforce PBT rules only for pure functions and serialization round-trips (suitable for projects with limited algorithmic complexity)
C) No — skip all PBT rules (suitable for simple CRUD applications, UI-only projects, or thin integration layers with no significant business logic)
D) Other (please describe after [Answer]: tag below)

[Answer]: D , there should be school based testing. There should not be a property based testing. 

---

**Instructions**: Please fill in all `[Answer]:` tags above and let me know when you're done.
