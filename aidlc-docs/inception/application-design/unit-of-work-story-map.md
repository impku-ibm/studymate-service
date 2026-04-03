# Unit of Work — Requirements Mapping

Since User Stories were skipped, this maps Functional Requirements (from requirements.md) to units.

| Requirement | Unit(s) | Coverage |
|------------|---------|----------|
| FR-01: Multi-Board Grading | Unit 9 | GradingScale entity, GradingService, grade calculation |
| FR-02: Configurable Classes | Already built | No changes needed |
| FR-03: Configurable Academic Year | Already built | No changes needed |
| FR-04: Exams UI | Unit 3 | Full exam lifecycle UI + enhanced backend |
| FR-05: Attendance UI | Unit 4 | Student + teacher + staff attendance UI |
| FR-06: Student/Parent Portal | Unit 10 | Read-only portal with dashboard |
| FR-07: PDF Report Cards | Unit 9 | ReportCardService + PDF generation |
| FR-08: Enhanced Fee Types | Unit 2 | Enum extension in migration |
| FR-09: Fee Concessions/Discounts | Unit 5 | FeeDiscount entity + service |
| FR-10: Payment Modes | Unit 2 | Enum extension in migration |
| FR-11: Student Promotion | Unit 8 | PromotionService + UI |
| FR-12: Transfer Certificate | Unit 8 | TC generation + PDF |
| FR-13: Timetable Management | Unit 7 | Full timetable module |
| FR-14: Bug Fixes | Unit 1 | All critical bugs fixed |
| FR-15: Notification System Design | Unit 5 (interface) | NotificationService interface defined |

## Additional Requirements from Clarification

| Requirement | Unit(s) | Coverage |
|------------|---------|----------|
| Fee Plans (student categories) | Unit 5 | FeePlan entity, FeePlanService, assignment to students |
| Monthly auto fee generation | Unit 5 | FeeSchedulerService (@Scheduled) |
| Fee dashboard + unpaid highlighting | Unit 5 | Enhanced accounts dashboard |
| CSV marks upload | Unit 3 | Upload endpoint + CSV parser |
| Grace marks | Unit 3 | Admin grace marks before publishing |
| Staff module | Unit 6 | Staff entity, CRUD, self-attendance |
| Teacher self-attendance | Unit 4 | Self-mark endpoint + UI |
| Teacher portal | Unit 10 | Separate route structure for teachers |
| Admission → auto fee generation | Unit 5 | Event-driven on enrollment |

## Coverage Verification

- All 15 functional requirements mapped: ✅
- All 11 clarification requirements mapped: ✅
- No orphan requirements: ✅
- No unit without requirements: ✅
