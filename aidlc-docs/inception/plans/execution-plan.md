# Execution Plan

## Detailed Analysis Summary

### Transformation Scope
- **Transformation Type**: Multi-module enhancement + new modules
- **Primary Changes**: Add 3 new UI modules (Exams, Attendance, Student Portal), 3 new backend modules (Timetable, Promotion, TC), enhance 2 existing modules (Fees, Grading), fix 6 bugs
- **Related Components**: All 12 existing backend modules + 3 new ones, all frontend pages + 7 new ones

### Change Impact Assessment
- **User-facing changes**: Yes — 7 new pages, 3 new user flows (Exams, Attendance, Student Portal)
- **Structural changes**: Yes — new Timetable module, Grading system, Student Portal route structure
- **Data model changes**: Yes — 7 new entities, 5 enum extensions, ~6 new database tables
- **API changes**: Yes — ~15 new endpoints, 5 missing endpoints to add
- **NFR impact**: Yes — security rules enforced, PDF generation, pagination required

### Risk Assessment
- **Risk Level**: Medium — multiple modules but well-understood domain, existing patterns to follow
- **Rollback Complexity**: Moderate — Flyway migrations are forward-only, but changes are additive (no breaking changes to existing)
- **Testing Complexity**: Complex — multi-role testing (ADMIN, TEACHER, STUDENT), cross-module integration

---

## Workflow Visualization

```
INCEPTION PHASE (Planning)
  [x] Workspace Detection .............. COMPLETED
  [x] Reverse Engineering .............. COMPLETED
  [x] Requirements Analysis ............ COMPLETED
  [ ] User Stories ..................... SKIP
  [x] Workflow Planning ................ IN PROGRESS
  [ ] Application Design ............... EXECUTE
  [ ] Units Generation ................. EXECUTE

CONSTRUCTION PHASE (Build)
  Per Unit:
    [ ] Functional Design .............. EXECUTE (per unit)
    [ ] NFR Requirements ............... SKIP
    [ ] NFR Design ..................... SKIP
    [ ] Infrastructure Design .......... SKIP
    [ ] Code Generation ................ EXECUTE (per unit)
  [ ] Build and Test ................... EXECUTE

OPERATIONS PHASE
  [ ] Operations ....................... PLACEHOLDER
```

---

## Phases to Execute

### INCEPTION PHASE
- [x] Workspace Detection (COMPLETED)
- [x] Reverse Engineering (COMPLETED)
- [x] Requirements Analysis (COMPLETED)
- [ ] User Stories — SKIP
  - **Rationale**: Single developer project, requirements are clear, no multi-team coordination needed
- [x] Workflow Planning (IN PROGRESS)
- [ ] Application Design — EXECUTE
  - **Rationale**: 7 new entities, 3 new modules, new route structure for Student Portal — need component design before coding
- [ ] Units Generation — EXECUTE
  - **Rationale**: Scope is large (15 FRs). Need to decompose into parallel units of work with clear dependencies

### CONSTRUCTION PHASE (Per Unit)
- [ ] Functional Design — EXECUTE (per unit)
  - **Rationale**: New data models, business rules (grading calculation, fee discounts, promotion logic) need detailed design
- [ ] NFR Requirements — SKIP
  - **Rationale**: NFRs are already defined in requirements.md; existing tech stack is sufficient; no new infrastructure decisions needed
- [ ] NFR Design — SKIP
  - **Rationale**: Skipped because NFR Requirements is skipped
- [ ] Infrastructure Design — SKIP
  - **Rationale**: Running locally for now; existing Docker Compose + Neon + Atlas setup is sufficient; no infra changes needed
- [ ] Code Generation — EXECUTE (per unit, always)
  - **Rationale**: Core implementation stage — planning + code generation for each unit
- [ ] Build and Test — EXECUTE (always)
  - **Rationale**: Build verification, integration testing, school-based testing

---

## Proposed Units of Work

Based on dependency analysis, the work decomposes into these units:

| # | Unit | Dependencies | Priority |
|---|------|-------------|----------|
| 1 | Bug Fixes + Missing Backend Endpoints | None | Critical (unblocks other units) |
| 2 | Enum Extensions + New Entities (DB migrations) | Unit 1 | Critical (unblocks units 3-7) |
| 3 | Exams UI + Exam API enhancements | Unit 2 | High |
| 4 | Attendance UI + Attendance API | Unit 2 | High (parallel with Unit 3) |
| 5 | Fee Enhancements (discounts, new types) | Unit 2 | High (parallel with Units 3-4) |
| 6 | Student/Parent Portal | Units 3, 4, 5 | Medium (needs data from exams, attendance, fees) |
| 7 | Timetable Management | Unit 2 | Medium (parallel with Unit 6) |
| 8 | Student Promotion + Transfer Certificate | Unit 2 | Medium (parallel with Units 6-7) |
| 9 | Grading System (multi-board configurable) | Unit 3 | Medium (enhances exam results) |
| 10 | PDF Report Card Generation | Units 3, 9 | Low (needs exams + grading first) |

### Recommended Execution Order
```
Phase 1 (Foundation):  Unit 1 → Unit 2
Phase 2 (Core):        Unit 3 + Unit 4 + Unit 5 (parallel)
Phase 3 (Portal):      Unit 6 + Unit 7 + Unit 8 (parallel)
Phase 4 (Polish):      Unit 9 → Unit 10
```

---

## Estimated Timeline
- **Total Units**: 10
- **Phase 1 (Foundation)**: Bug fixes, migrations, enum changes
- **Phase 2 (Core)**: Exams UI, Attendance UI, Fee enhancements
- **Phase 3 (Portal)**: Student Portal, Timetable, Promotion/TC
- **Phase 4 (Polish)**: Grading system, PDF report cards

## Success Criteria
- **Primary Goal**: Production-grade Indian School ERP running locally with all modules functional
- **Key Deliverables**:
  - All 15 functional requirements implemented
  - All existing bugs fixed
  - All 7 new frontend pages working
  - All ~15 new API endpoints functional
  - Security rules compliant
- **Quality Gates**:
  - All pages load without errors
  - All API endpoints return correct responses
  - Multi-role access works (ADMIN, TEACHER, STUDENT)
  - School context isolation verified
  - PDF generation works for report cards and TCs
