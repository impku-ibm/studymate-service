# Code Quality Assessment

## Test Coverage
- **Overall**: Poor/None — No test files found in frontend; backend has test dependencies but no visible test classes
- **Unit Tests**: Not implemented
- **Integration Tests**: Not implemented
- **E2E Tests**: Not implemented

## Code Quality Indicators

### Backend
- **Linting**: Not configured (no Checkstyle, SpotBugs, or PMD)
- **Code Style**: Generally consistent — Lombok used throughout, layered architecture followed
- **Documentation**: Swagger/OpenAPI annotations on newer modules (Accounts, Exam, Attendance); older modules lack annotations
- **Validation**: Jakarta Bean Validation used on DTOs (`@NotBlank`, `@NotNull`, `@Email`, `@Positive`)
- **Error Handling**: Global exception handler exists in `common/exception/`
- **Logging**: SLF4J with structured logging in newer modules; inconsistent in older ones

### Frontend
- **Linting**: ESLint configured (`eslint.config.js`)
- **Code Style**: Consistent Tailwind utility classes, consistent component structure
- **Documentation**: No JSDoc or inline documentation
- **State Management**: Minimal — only AcademicYearContext; no global error/loading state
- **Error Handling**: Basic try/catch with `console.error` and `alert()` — not production-grade

## Technical Debt

### Critical Issues
1. **SchoolOnboarding.jsx**: `useState` not imported; form inputs not wired to state; posts to wrong endpoint (`/schools` vs `/school`)
2. **FeeCollection.jsx**: Calls `/api/classes` but backend is at `/classes` (no `/api` prefix) — will 404
3. **accountsApi.js**: Has `updateFeeStructure`, `deleteFeeStructure`, `toggleFeeStructure` but backend has no matching PUT/DELETE/PATCH endpoints
4. **AcademicYearSetup.jsx**: Calls `PATCH /academic-years/{id}/activate` but no such endpoint exists in AcademicYearController

### Moderate Issues
5. **API path inconsistency**: Core modules use `/resource`, accounts uses `/api/v1/accounts/...`, exams use `/api/exams`, attendance uses `/api/attendance`
6. **No centralized API layer**: Only accounts has a dedicated API file; all other modules make inline axios calls
7. **Exam module entities don't use Lombok**: Manual getters/setters while rest of codebase uses `@Getter/@Setter/@Builder`
8. **Sidebar "Exams" link**: Points to `/admin/exams` but no route exists in App.jsx
9. **ClassSubjectSetup**: Component exists but not wired into SchoolSetup tabs
10. **No Exams or Attendance frontend**: Backend APIs exist but zero UI components

### Minor Issues
11. **Topbar**: Hardcoded "Admin User" and "A" avatar — should use actual user data
12. **No loading/error toasts**: Components use `alert()` for errors
13. **No pagination**: Frontend tables load all data at once (will be slow with large datasets)
14. **Redis not configured for local profile**: `application-local.yaml` has no Redis config — rate limiting may fail locally

## Patterns and Anti-patterns

### Good Patterns
- Consistent layered architecture (Controller -> Service -> Repository)
- DTO separation from entities
- School context isolation via ThreadLocal
- JWT-based stateless auth
- Flyway for database migrations
- Event-driven fee generation
- Role-based access control with `@PreAuthorize`

### Anti-patterns
- Inline API calls in React components (should use centralized API services)
- `alert()` for error handling in frontend
- No input sanitization beyond validation
- Hardcoded CORS origin (`http://localhost:5173`)
- No environment-based frontend config (API URL hardcoded in axios.js)
- Missing `@Transactional` annotations visible in some services
