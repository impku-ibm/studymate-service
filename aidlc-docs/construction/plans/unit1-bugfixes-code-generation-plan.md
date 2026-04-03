# Unit 1: Bug Fixes + Missing Backend Endpoints — Code Generation Plan

## Unit Context
- **Unit**: Bug Fixes + Missing Backend Endpoints
- **Priority**: Critical (unblocks all other units)
- **Dependencies**: None
- **Requirements**: FR-14 (Bug Fixes)

## Steps

### Backend Fixes
- [x] Step 1: Add `PATCH /academic-years/{id}/activate` endpoint to AcademicYearController + AcademicYearService
- [x] Step 2: Add `PUT /api/v1/accounts/fee-structures/{id}` endpoint to AccountsController + FeeStructureService
- [x] Step 3: Add `DELETE /api/v1/accounts/fee-structures/{id}` endpoint to AccountsController + FeeStructureService
- [x] Step 4: Add `PATCH /api/v1/accounts/fee-structures/{id}/toggle` endpoint to AccountsController + FeeStructureService
- [x] Step 5: Add `GET /api/v1/accounts/student-fees/class/{classId}` endpoint to AccountsController + StudentFeeService
- [x] Step 6: Add PATCH to allowed CORS methods in SecurityConfig

### Frontend Fixes
- [x] Step 7: Fix `SchoolOnboarding.jsx` — import useState, wire form inputs to state, fix endpoint `/schools` → `/school`
- [x] Step 8: Fix `FeeCollection.jsx` — change `/api/classes` → `/classes`
- [x] Step 9: Fix `Topbar.jsx` — show actual user name from JWT localStorage
- [x] Step 10: Add ClassSubjectSetup tab to SchoolSetup.jsx
- [x] Step 11: Wire `/admin/exams` route in App.jsx with placeholder Exams page
- [x] Step 12: Add Attendance sidebar link in Sidebar.jsx

### Documentation
- [x] Step 13: Update aidlc-state.md and audit.md
