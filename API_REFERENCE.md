# StudyMate ERP - API Request/Response Reference

## 📘 EXAMS MODULE APIs

### 1. Create Exam
**Endpoint**: `POST /api/exams`
**Auth**: ADMIN role required

**Request**:
```json
{
  "academicYearId": 1,
  "examType": "MID_TERM",
  "name": "Mid Term Examination 2024",
  "startDate": "2024-12-15",
  "endDate": "2024-12-25"
}
```

**Response** (201 Created):
```json
{
  "id": 101,
  "academicYearId": 1,
  "academicYearName": "2024-2025",
  "examType": "MID_TERM",
  "name": "Mid Term Examination 2024",
  "startDate": "2024-12-15",
  "endDate": "2024-12-25",
  "publishResult": false,
  "status": "DRAFT"
}
```

---

### 2. Schedule Exam
**Endpoint**: `POST /api/exams/schedule`
**Auth**: ADMIN role required

**Request**:
```json
{
  "examId": 101,
  "classId": 5,
  "section": "A",
  "subjectId": 10,
  "examDate": "2024-12-16",
  "maxMarks": 100,
  "passMarks": 40,
  "durationMinutes": 120
}
```

**Response** (200 OK):
```json
{
  "id": 201,
  "examId": 101,
  "classId": 5,
  "className": "Class 10",
  "section": "A",
  "subjectId": 10,
  "subjectName": "Mathematics",
  "examDate": "2024-12-16",
  "maxMarks": 100,
  "passMarks": 40,
  "durationMinutes": 120
}
```

---

### 3. Enter Marks
**Endpoint**: `POST /api/exams/marks`
**Auth**: TEACHER role required

**Request**:
```json
{
  "examScheduleId": 201,
  "studentId": 50,
  "marksObtained": 85,
  "absent": false,
  "remarks": "Good performance"
}
```

**Response** (200 OK):
```json
{
  "message": "Marks saved successfully"
}
```

**Error Response** (400 Bad Request):
```json
{
  "error": "Marks cannot exceed maximum marks (100)",
  "timestamp": "2024-12-16T10:30:00Z"
}
```

---

### 4. Publish Results
**Endpoint**: `POST /api/exams/{examId}/publish`
**Auth**: ADMIN role required
**Path**: `/api/exams/101/publish`

**Response** (200 OK):
```json
{
  "message": "Results published successfully",
  "examId": 101,
  "totalStudents": 45,
  "resultsGenerated": 45
}
```

---

### 5. Get All Exams
**Endpoint**: `GET /api/exams`
**Auth**: ADMIN, TEACHER role required

**Response** (200 OK):
```json
[
  {
    "id": 101,
    "academicYearId": 1,
    "academicYearName": "2024-2025",
    "examType": "MID_TERM",
    "name": "Mid Term Examination 2024",
    "startDate": "2024-12-15",
    "endDate": "2024-12-25",
    "publishResult": true,
    "status": "PUBLISHED"
  },
  {
    "id": 102,
    "academicYearId": 1,
    "academicYearName": "2024-2025",
    "examType": "FINAL",
    "name": "Final Examination 2024",
    "startDate": "2025-03-01",
    "endDate": "2025-03-15",
    "publishResult": false,
    "status": "DRAFT"
  }
]
```

---

## 📗 ATTENDANCE MODULE APIs

### 1. Mark Attendance
**Endpoint**: `POST /api/attendance/mark`
**Auth**: TEACHER role required

**Request**:
```json
{
  "classId": 5,
  "section": "A",
  "attendanceDate": "2024-12-16",
  "attendanceEntries": [
    {
      "studentId": 50,
      "status": "PRESENT"
    },
    {
      "studentId": 51,
      "status": "ABSENT"
    },
    {
      "studentId": 52,
      "status": "LEAVE"
    }
  ]
}
```

**Response** (200 OK):
```json
{
  "message": "Attendance marked successfully",
  "totalMarked": 3,
  "date": "2024-12-16"
}
```

---

### 2. Get Attendance by Date & Section
**Endpoint**: `GET /api/attendance/date/{date}/section/{section}`
**Auth**: TEACHER, ADMIN role required
**Example**: `/api/attendance/date/2024-12-16/section/A`

**Response** (200 OK):
```json
[
  {
    "id": 301,
    "studentId": 50,
    "studentName": "Raj Kumar",
    "attendanceDate": "2024-12-16",
    "status": "PRESENT",
    "section": "A",
    "markedByTeacher": "Mrs. Sharma"
  },
  {
    "id": 302,
    "studentId": 51,
    "studentName": "Priya Singh",
    "attendanceDate": "2024-12-16",
    "status": "ABSENT",
    "section": "A",
    "markedByTeacher": "Mrs. Sharma"
  }
]
```

---

### 3. Get Student Attendance Summary
**Endpoint**: `GET /api/attendance/student/{studentId}/summary?month=2024-12`
**Auth**: TEACHER, ADMIN, STUDENT role required
**Example**: `/api/attendance/student/50/summary?month=2024-12`

**Response** (200 OK):
```json
{
  "studentId": 50,
  "studentName": "Raj Kumar",
  "totalDays": 20,
  "presentDays": 18,
  "absentDays": 1,
  "leaveDays": 1,
  "attendancePercentage": 90.00
}
```

---

### 4. Get Attendance History
**Endpoint**: `GET /api/attendance/student/{studentId}/history?startDate=2024-12-01&endDate=2024-12-31`
**Auth**: TEACHER, ADMIN, STUDENT role required

**Response** (200 OK):
```json
[
  {
    "id": 301,
    "studentId": 50,
    "studentName": "Raj Kumar",
    "attendanceDate": "2024-12-01",
    "status": "PRESENT",
    "section": "A",
    "markedByTeacher": "Mrs. Sharma"
  },
  {
    "id": 302,
    "studentId": 50,
    "studentName": "Raj Kumar",
    "attendanceDate": "2024-12-02",
    "status": "PRESENT",
    "section": "A",
    "markedByTeacher": "Mrs. Sharma"
  }
]
```

---

## 💰 FEES MODULE APIs

### 1. Create Transport Fee Estimation
**Endpoint**: `POST /api/transport-fee-estimation`
**Auth**: ADMIN role required

**Request**:
```json
{
  "distanceSlab": "5-10km",
  "minFee": 1500,
  "maxFee": 2000,
  "busRouteName": "Route A - North Zone",
  "pickupZone": "Sector 5"
}
```

**Response** (201 Created):
```json
{
  "id": 401,
  "distanceSlab": "5-10km",
  "minFee": 1500,
  "maxFee": 2000,
  "busRouteName": "Route A - North Zone",
  "pickupZone": "Sector 5",
  "suggestedFee": 1750
}
```

---

### 2. Get All Transport Estimations
**Endpoint**: `GET /api/transport-fee-estimation`
**Auth**: ADMIN, TEACHER role required

**Response** (200 OK):
```json
[
  {
    "id": 401,
    "distanceSlab": "0-5km",
    "minFee": 500,
    "maxFee": 1000,
    "busRouteName": "Route A - Local",
    "pickupZone": "Sector 1",
    "suggestedFee": 750
  },
  {
    "id": 402,
    "distanceSlab": "5-10km",
    "minFee": 1500,
    "maxFee": 2000,
    "busRouteName": "Route B - North",
    "pickupZone": "Sector 5",
    "suggestedFee": 1750
  },
  {
    "id": 403,
    "distanceSlab": "10-20km",
    "minFee": 2500,
    "maxFee": 3500,
    "busRouteName": "Route C - Far",
    "pickupZone": "Sector 10",
    "suggestedFee": 3000
  }
]
```

---

### 3. Get Estimation by Distance Slab
**Endpoint**: `GET /api/transport-fee-estimation/distance-slab/{distanceSlab}`
**Auth**: ADMIN, TEACHER role required
**Example**: `/api/transport-fee-estimation/distance-slab/5-10km`

**Response** (200 OK):
```json
{
  "id": 402,
  "distanceSlab": "5-10km",
  "minFee": 1500,
  "maxFee": 2000,
  "busRouteName": "Route B - North",
  "pickupZone": "Sector 5",
  "suggestedFee": 1750
}
```

---

## 🔄 EXISTING FEES APIs (Enhanced)

### Get Student Fees
**Endpoint**: `GET /api/accounts/student/{studentId}/fees?page=0&size=10`
**Auth**: ADMIN, TEACHER, STUDENT role required

**Response** (200 OK):
```json
{
  "content": [
    {
      "id": 501,
      "studentId": 50,
      "studentName": "Raj Kumar",
      "feeType": "ADMISSION",
      "amount": 5000,
      "dueDate": "2024-06-30",
      "paidAmount": 5000,
      "pendingAmount": 0,
      "status": "PAID",
      "paidDate": "2024-06-15"
    },
    {
      "id": 502,
      "studentId": 50,
      "studentName": "Raj Kumar",
      "feeType": "TUITION",
      "amount": 50000,
      "dueDate": "2024-12-31",
      "paidAmount": 25000,
      "pendingAmount": 25000,
      "status": "PARTIAL",
      "paidDate": "2024-11-15"
    },
    {
      "id": 503,
      "studentId": 50,
      "studentName": "Raj Kumar",
      "feeType": "EXAM",
      "amount": 2000,
      "dueDate": "2024-12-20",
      "paidAmount": 0,
      "pendingAmount": 2000,
      "status": "PENDING",
      "paidDate": null
    },
    {
      "id": 504,
      "studentId": 50,
      "studentName": "Raj Kumar",
      "feeType": "TRANSPORT",
      "amount": 1750,
      "dueDate": "2024-12-31",
      "paidAmount": 0,
      "pendingAmount": 1750,
      "status": "PENDING",
      "paidDate": null
    }
  ],
  "totalElements": 4,
  "totalPages": 1,
  "currentPage": 0
}
```

---

## 🔐 ERROR RESPONSES

### 400 Bad Request
```json
{
  "error": "Validation failed",
  "details": {
    "examType": "Exam type already exists for this academic year",
    "endDate": "End date cannot be before start date"
  },
  "timestamp": "2024-12-16T10:30:00Z"
}
```

### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "Invalid or expired token",
  "timestamp": "2024-12-16T10:30:00Z"
}
```

### 403 Forbidden
```json
{
  "error": "Forbidden",
  "message": "You don't have permission to perform this action",
  "timestamp": "2024-12-16T10:30:00Z"
}
```

### 404 Not Found
```json
{
  "error": "Not Found",
  "message": "Exam not found",
  "timestamp": "2024-12-16T10:30:00Z"
}
```

### 409 Conflict
```json
{
  "error": "Conflict",
  "message": "Exam type already exists for this academic year",
  "timestamp": "2024-12-16T10:30:00Z"
}
```

---

## 📝 COMMON HEADERS

### Request Headers
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
Accept: application/json
```

### Response Headers
```
Content-Type: application/json
X-Total-Count: 100
X-Page-Number: 0
X-Page-Size: 10
```

---

## 🔄 PAGINATION PATTERN

### Query Parameters
```
?page=0&size=10&sort=name,asc
```

### Response Format
```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 10,
  "currentPage": 0,
  "pageSize": 10,
  "hasNext": true,
  "hasPrevious": false
}
```

---

## 📊 FILTER PATTERNS

### Exams Filter
```
GET /api/exams?academicYearId=1&examType=MID_TERM&status=PUBLISHED
```

### Attendance Filter
```
GET /api/attendance/date/2024-12-16/section/A?classId=5
```

### Fees Filter
```
GET /api/accounts/student/50/fees?feeType=TUITION&status=PENDING
```

---

## 🎯 UI INTEGRATION CHECKLIST

- [ ] Implement JWT token storage (localStorage/sessionStorage)
- [ ] Create API service layer with interceptors
- [ ] Handle 401 errors with token refresh
- [ ] Implement loading states for all API calls
- [ ] Add error toast notifications
- [ ] Implement pagination for list views
- [ ] Add date pickers for date fields
- [ ] Create dropdown components for enums
- [ ] Implement form validation
- [ ] Add confirmation dialogs for destructive actions
- [ ] Implement export to PDF/Excel
- [ ] Add real-time notifications
- [ ] Create responsive tables
- [ ] Implement search/filter functionality
- [ ] Add audit log display

This reference covers all API endpoints needed for UI integration!