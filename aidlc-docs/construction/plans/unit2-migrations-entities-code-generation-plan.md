# Unit 2: Database Migrations + New Entities + Enum Extensions — Code Generation Plan

## Unit Context
- **Unit**: DB Migrations + Entities + Enums
- **Priority**: Critical (unblocks Units 3-10)
- **Dependencies**: Unit 1 (completed)

## Steps

### Enum Extensions
- [x] Step 1: Extend ExamType enum (add FA1, FA2, SA1, SA2)
- [x] Step 2: Extend FeeType enum (add 8 new types)
- [x] Step 3: Extend PaymentMode enum (add CHEQUE, DEMAND_DRAFT, ONLINE)
- [x] Step 4: Add TRANSFERRED to StudentStatus enum
- [x] Step 5: Add STAFF to Role enum
- [x] Step 6: Create new enums: StaffType, FeeFrequency, DiscountType

### Flyway Migrations
- [x] Step 7: V9 — grading_scale + grading_scale_entry tables
- [x] Step 8: V10 — fee_plan + fee_plan_item tables + add fee_plan_id to student
- [x] Step 9: V11 — fee_discount table
- [x] Step 10: V12 — period_definition + timetable_entry tables
- [x] Step 11: V13 — transfer_certificate table
- [x] Step 12: V14 — staff + staff_attendance tables

### New Entity Classes + Repositories
- [x] Step 13: GradingScale + GradingScaleEntry entities + GradingScaleRepository
- [x] Step 14: FeePlan + FeePlanItem entities + FeePlanRepository
- [x] Step 15: FeeDiscount entity + FeeDiscountRepository
- [x] Step 16: PeriodDefinition + TimetableEntry entities + repositories
- [x] Step 17: TransferCertificate entity + TransferCertificateRepository
- [x] Step 18: Staff + StaffAttendance entities + repositories
- [x] Step 18b: Add feePlanId to Student entity

### Documentation
- [x] Step 19: Update state and audit
