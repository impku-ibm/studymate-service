# Application Design — Clarification Questions

Based on your feedback, I need to clarify a few specifics before updating the design. Please answer each question.

---

## Section 1: Student Admission & Fee Flow

## Question 1
When a new student is admitted, what fees should be auto-generated?

A) Only admission fee (one-time), then monthly fees are generated separately by admin
B) Admission fee + full year's fees (tuition, exam, etc.) all generated at enrollment time
C) Admission fee + first month's fees, then remaining months generated monthly (auto or manual)
D) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 2
How should monthly fee collection work?

A) Admin manually generates fees each month for all students (click "Generate Monthly Fees")
B) System auto-generates monthly fees at the start of each month (scheduled job)
C) All fees for the entire year are generated upfront at enrollment, with monthly due dates
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 3
Should the system track fee payment status per student with reminders?

A) Yes — show overdue fees on dashboard, highlight unpaid students in red
B) Yes — plus send notification to parents when fee is overdue (future notification system)
C) Just track paid/unpaid status, no active reminders
D) Other (please describe after [Answer]: tag below)

[Answer]: D , MAINTAIN A DASHBOARD , HIGHLIGHT UNPAID STUDENTS , PLUS SEND NOTIFICTAION TO PARENTS

---

## Section 2: Student Categories & Variable Fees

## Question 4
How should the system handle different student categories (hosteller, transport, day scholar)?

A) Add a "Student Category" field on Student entity (HOSTELLER, TRANSPORT, DAY_SCHOLAR) — fee structure varies by category
B) Use fee type assignment per student — admin manually assigns which fee types apply to each student (e.g., hosteller gets HOSTEL + TUITION, day scholar gets only TUITION)
C) Use a "Fee Plan" concept — admin creates fee plans (Hosteller Plan, Transport Plan, Day Scholar Plan) with different fee types and amounts, then assigns a plan to each student
D) Other (please describe after [Answer]: tag below)

[Answer]: 

---

## Section 3: Marks Entry & Teacher-Subject Mapping

## Question 5
For marks entry, how should teacher-subject-class mapping work?

A) Only the teacher assigned to a subject for a class/section (via TeacherAssignment) can enter marks for that subject — strict enforcement
B) Any teacher can enter marks for any subject (admin-level flexibility)
C) Assigned teacher enters marks, but admin can also enter/override marks for any subject
D) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 6
How should marks be uploaded?

A) Manual entry only — teacher fills marks one by one in a grid on screen
B) Manual entry + bulk CSV upload option (teacher uploads a spreadsheet)
C) Manual entry only for now, CSV upload added later
D) Other (please describe after [Answer]: tag below)

[Answer]: D , Option to enter through csv as well as by screen so that it will be easier for the teacher.

---

## Section 4: Result Publishing

## Question 7
How should result publishing work?

A) Admin publishes results for an entire exam at once (all classes, all subjects)
B) Admin publishes per class/section (e.g., publish Class 10-A results first, then 10-B)
C) Results auto-publish once all marks are entered for all subjects in a class
D) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 8
Should the system support grace marks?

A) Yes — admin can add grace marks to individual students before publishing
B) Yes — system auto-applies grace marks based on rules (e.g., +5 marks if within 2 marks of passing)
C) No — marks are final as entered
D) Other (please describe after [Answer]: tag below)

[Answer]: A

---

## Section 5: Teacher Management & Staff

## Question 9
Should the system track non-teaching staff (peon, clerk, accountant, librarian)?

A) Yes — add a "Staff" module separate from Teachers, with attendance tracking
B) Yes — extend the Teacher entity with a "staffType" field (TEACHING, NON_TEACHING)
C) No — only track teachers for now, staff management added later
D) Other (please describe after [Answer]: tag below)

[Answer]:A 

## Question 10
How should teacher availability/routine work?

A) Teachers have a timetable (already planned) — availability is derived from free periods
B) Teachers manually mark their availability (e.g., "available Monday 10am-12pm for substitution")
C) Just timetable is enough — no separate availability tracking needed
D) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 11
Should teacher attendance be separate from student attendance?

A) Yes — separate attendance page for teachers/staff with its own flow (admin marks teacher attendance daily)
B) Yes — but teachers self-mark their attendance (check-in/check-out)
C) Teacher attendance is already in the DB schema (teacher_attendance table) — just build the UI for admin to mark it
D) Other (please describe after [Answer]: tag below)

[Answer]: D , IF IT IS THERE MAKE IT LIKE EACH TEACHER CAN MARK THEIR ATTENDANCE.

---

**Instructions**: Please fill in all `[Answer]:` tags above and let me know when you're done. I'll update the application design based on your answers.
