# Application Design — Component Methods (Revised)

## 1. GradingService
```java
GradingScaleResponse createGradingScale(CreateGradingScaleRequest req)
List<GradingScaleResponse> getGradingScales()
GradingScaleResponse updateGradingScale(Long id, UpdateGradingScaleRequest req)
void deleteGradingScale(Long id)
String calculateGrade(Long schoolId, BigDecimal percentage)
```

## 2. FeePlanService
```java
FeePlanResponse createFeePlan(CreateFeePlanRequest req)
List<FeePlanResponse> getFeePlans()
FeePlanResponse getFeePlan(Long planId)
FeePlanResponse updateFeePlan(Long id, UpdateFeePlanRequest req)
void deleteFeePlan(Long id)
void assignPlanToStudent(Long studentId, Long feePlanId)
FeePlanResponse getStudentPlan(Long studentId)
```

## 3. FeeSchedulerService
```java
@Scheduled(cron = "0 0 1 1 * *")
void generateMonthlyFeesForAllSchools()
// Iterates all schools, all active enrollments, generates fees per student's plan

void generateMonthlyFeesForSchool(Long schoolId)
// Manual trigger for a specific school (admin action)
```

## 4. TimetableService
```java
PeriodDefinitionResponse createPeriod(CreatePeriodDefinitionRequest req)
List<PeriodDefinitionResponse> getPeriods()
void deletePeriod(Long id)
TimetableEntryResponse createEntry(CreateTimetableEntryRequest req)
List<TimetableEntryResponse> getClassTimetable(Long classId, String section)
List<TimetableEntryResponse> getTeacherTimetable(Long teacherId)
void deleteEntry(Long id)
List<String> validateNoConflicts(CreateTimetableEntryRequest req)
```

## 5. PromotionService
```java
PromotionResultResponse bulkPromote(BulkPromotionRequest req)
// req: sourceClassId, sourceSectionId, targetClassId, targetSectionId, targetAcademicYearId, List<studentIds>
// Returns: promotedCount, skippedCount, errors
```

## 6. TransferCertificateService
```java
TransferCertificateResponse generateTC(Long studentId, GenerateTCRequest req)
byte[] downloadTCPdf(Long studentId)
```

## 7. FeeDiscountService
```java
FeeDiscountResponse createDiscount(CreateFeeDiscountRequest req)
List<FeeDiscountResponse> getStudentDiscounts(Long studentId)
void deleteDiscount(Long discountId)
BigDecimal calculateDiscountedAmount(BigDecimal originalAmount, Long studentId, Long academicYearId)
```

## 8. ReportCardService
```java
byte[] generateReportCardPdf(Long examId, Long studentId)
byte[] generateClassReportCards(Long examId, Long classId, String section) // ZIP
```

## 9. StaffService
```java
StaffResponse createStaff(CreateStaffRequest req)
List<StaffResponse> getAllStaff()
StaffResponse updateStaff(Long id, UpdateStaffRequest req)
void markStaffSelfAttendance(MarkSelfAttendanceRequest req)
List<StaffAttendanceResponse> getStaffAttendance(Long staffId, YearMonth month)
StaffAttendanceSummary getStaffAttendanceSummary(Long staffId, YearMonth month)
```

## 10. ExamService (Enhanced Methods)
```java
// Existing methods remain. New additions:
List<ExamScheduleResponse> getSchedulesForExam(Long examId)
List<StudentResultResponse> getClassResults(Long examId, Long classId, String section)
StudentResultResponse getStudentResult(Long examId, Long studentId)

// CSV upload
void uploadMarksFromCSV(Long examScheduleId, MultipartFile csvFile)
// Parses CSV with columns: admissionNumber, marksObtained, absent, remarks
// Validates: student exists, marks <= maxMarks, no duplicates
// Bulk saves ExamMarks

// Grace marks
void addGraceMarks(Long examId, List<GraceMarkEntry> entries)
// GraceMarkEntry: studentId, subjectId, additionalMarks
// Updates ExamMarks.marksObtained += additionalMarks
// Audit logged

// Marks entry validation
// enterMarks now checks: is caller the assigned teacher for this subject/class/section?
// If not assigned teacher, must have ADMIN role
```

## 11. AttendanceService (Enhanced Methods)
```java
// Existing methods remain. New additions:
void markTeacherSelfAttendance(MarkSelfAttendanceRequest req)
// Extracts teacherId from JWT, validates can only mark own attendance
// Creates TeacherAttendance record
```

## 12. StudentFeeService (Enhanced Methods)
```java
// Existing methods remain. New additions:
List<StudentFeeResponse> getStudentFeesByClass(Long classId, Long academicYearId)

void generateAdmissionAndFirstMonthFees(Long studentId, Long feePlanId)
// Called on enrollment event
// Creates: admission fee (ONE_TIME) + first month's fees (MONTHLY items from plan)
// Applies discounts via FeeDiscountService
```

## 13. AcademicYearService (Enhanced Methods)
```java
AcademicYearResponse activateAcademicYear(Long id)
// Deactivates current active year, activates specified one
```

---

## Frontend API Service Methods

### examApi.js
```javascript
getExams()
createExam(payload)
scheduleExam(payload)
getSchedules(examId)
enterMarks(payload)
uploadMarksCSV(examScheduleId, file)     // NEW: FormData with CSV
addGraceMarks(examId, entries)           // NEW
publishResults(examId)
getClassResults(examId, classId, section)
downloadReportCard(examId, studentId)
```

### attendanceApi.js
```javascript
markStudentAttendance(payload)
getByDateSection(date, section)
getStudentSummary(studentId, month)
getStudentHistory(studentId, start, end)
markTeacherSelfAttendance(payload)       // NEW
getTeacherAttendance(teacherId, month)   // NEW
```

### feePlanApi.js (NEW)
```javascript
getFeePlans()
createFeePlan(payload)
updateFeePlan(id, payload)
deleteFeePlan(id)
assignPlanToStudent(studentId, feePlanId)
getStudentPlan(studentId)
```

### staffApi.js (NEW)
```javascript
getAllStaff()
createStaff(payload)
updateStaff(id, payload)
markSelfAttendance(payload)
getStaffAttendance(staffId, month)
```

### timetableApi.js
```javascript
createPeriod(payload)
getPeriods()
deletePeriod(id)
createEntry(payload)
getClassTimetable(classId, section)
getTeacherTimetable(teacherId)
deleteEntry(id)
```

### studentPortalApi.js
```javascript
getMyDashboard()
getMyAttendance(month)
getMyFees()
getMyResults()
downloadMyReportCard(examId)
```
