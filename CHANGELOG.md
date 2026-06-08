# CHANGELOG.md

All notable implementation milestones and changes are documented here.

---

## [1.0.0] - 2026-06-07

### Authentication
- Implemented JWT-based authentication (register + login)
- Passwords hashed with BCrypt
- JWT token returned on successful login/register
- All protected endpoints require Bearer token

### Meeting Management
- Create meeting with title, participants, meeting date, and transcript
- Participants stored via `MeetingParticipant` entity (many-to-many with User)
- Transcript stored per entry via `Transcript` entity (timestamp, speaker, text)
- Get meeting by ID with full participant and transcript details
- Get all meetings with pagination (sorted by createdAt descending)

### AI Analysis
- `POST /api/meetings/:id/analyze` triggers Groq LLM (llama-3.3-70b-versatile)
- Generates summary, decisions, follow-ups, and action items from transcript
- Every insight includes transcript citations (timestamp references)
- Full analysis JSON saved to `MeetingAnalysis` entity
- Action items auto-extracted and saved to `action_items` table
- Re-analysis blocked — 409 CONFLICT if meeting already analyzed
- Due dates parsed from natural language (e.g. "15th June", "Monday") using today's date as reference

### Action Item Management
- Action items auto-created from AI analysis with citations
- Filter by status, assignee, or meeting ID
- Update action item status: PENDING → IN_PROGRESS → COMPLETED
- Overdue detection: status != COMPLETED AND dueDate < today

### Reminder Workflow
- Scheduled job identifies overdue action items
- Sends reminder via Telegram Bot API
- Reminder history stored in database
- `reminderSent` flag prevents duplicate notifications

### Non-Functional
- Unified API response format: `{ traceId, success, data/error }` on all endpoints
- Trace ID generated per request, included in logs and response
- Global exception handler — no raw errors returned to client
- Input validation on all request DTOs
- Swagger UI available at `/swagger-ui.html`
- `/health` and `/api/evaluation` endpoints implemented

### Security
- Hardcoded secrets removed from codebase
- Environment variables used for all secrets (DB, JWT, Groq, Telegram)
- `application-local.properties` gitignored

---

## [0.1.0] - Initial Setup

- Spring Boot 3.2.5 project initialized
- MySQL database connection configured
- JPA entities created: User, Meeting, MeetingParticipant, Transcript, ActionItem, MeetingAnalysis, ReminderLog
- Base repository, service, and controller layer structure set up
- Swagger/OpenAPI dependency added
