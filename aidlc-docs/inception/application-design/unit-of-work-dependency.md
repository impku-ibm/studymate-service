# Unit of Work — Dependency Matrix

## Dependency Graph

```
Unit 1 (Bug Fixes)
  |
  v
Unit 2 (DB Migrations + Entities)
  |
  +---> Unit 3 (Exams) --------+---> Unit 9 (Grading + Report Cards)
  |                             |
  +---> Unit 4 (Attendance) ----+---> Unit 10 (Student + Teacher Portal)
  |                             |
  +---> Unit 5 (Fee Plans) -----+
  |
  +---> Unit 6 (Staff)
  |
  +---> Unit 7 (Timetable)
  |
  +---> Unit 8 (Promotion + TC)
```

## Dependency Details

| Unit | Depends On | Reason |
|------|-----------|--------|
| 1 | None | Foundation — fixes existing code |
| 2 | Unit 1 | Needs bug fixes applied before adding new migrations |
| 3 | Unit 2 | Needs ExamType enum extensions, GradingScale entity |
| 4 | Unit 2 | Needs Staff entity for staff attendance, enum extensions |
| 5 | Unit 2 | Needs FeePlan, FeeDiscount entities, FeeType extensions |
| 6 | Unit 2 | Needs Staff, StaffAttendance entities, StaffType enum |
| 7 | Unit 2 | Needs PeriodDefinition, TimetableEntry entities |
| 8 | Unit 2 | Needs TransferCertificate entity, StudentStatus.TRANSFERRED |
| 9 | Unit 3 | Needs exam results data to generate report cards with grades |
| 10 | Units 3, 4, 5 | Student portal shows exams, attendance, fees — needs all three |

## Parallelization Opportunities

| Phase | Units | Can Run In Parallel | Notes |
|-------|-------|-------------------|-------|
| 1 | 1, 2 | No (sequential) | Unit 2 depends on Unit 1 |
| 2 | 3, 4, 5 | Yes | Independent modules, all depend only on Unit 2 |
| 3 | 6, 7, 8 | Yes | Independent modules, all depend only on Unit 2 |
| 4 | 9 | No (needs Unit 3) | Grading integrates with exam results |
| 5 | 10 | No (needs 3, 4, 5) | Portal aggregates data from core modules |

## Integration Testing Checkpoints

| After Phase | Test |
|------------|------|
| Phase 1 | All existing pages load without errors, fixed bugs verified |
| Phase 2 | Exam creation → scheduling → marks entry → publishing flow |
| Phase 2 | Attendance marking → summary → history flow |
| Phase 2 | Fee plan creation → student assignment → fee generation → payment flow |
| Phase 3 | Staff CRUD + self-attendance, Timetable CRUD + conflict detection, Promotion + TC |
| Phase 4 | Report card PDF generation with correct grades |
| Phase 5 | Student portal shows correct data, Teacher portal marks entry works |
