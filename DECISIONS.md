# DECISIONS.md — Technical Decision Log

This document records the key technical decisions made during the implementation of the Meeting Intelligence Service, including rationale, alternatives considered, and trade-offs.

---

## 1. Language & Framework — Java + Spring Boot

### Decision
Use **Java 17** with **Spring Boot 3.2.5** as the core framework.

### Why chosen
- Java is my primary language — reduces ramp-up time and risk of implementation errors
- Spring Boot provides production-ready features out of the box: dependency injection, security, validation, scheduling, JPA, and structured logging — all needed for this assignment
- Strong ecosystem with battle-tested libraries for every requirement (JWT, Swagger, WebClient)
- Spring Security integrates directly with JWT without additional boilerplate

### Alternatives considered
- **Node.js + Express** — lighter weight, faster to scaffold, but less structured. Spring Boot's built-in validation, security, and scheduling would all require separate libraries
- **Python + FastAPI** — excellent for AI-heavy workloads, clean syntax, but less familiar and more setup required for production-grade auth and scheduling

### Trade-offs
- Spring Boot has higher startup time and memory footprint vs Node.js
- More boilerplate than Express or FastAPI for simple endpoints
- Justified because the built-in features (Security, Scheduling, JPA) directly map to assignment requirements

---

## 2. Database — MySQL

### Decision
Use **MySQL 8** as the relational database with **Hibernate/JPA** as the ORM.

### Why chosen
- Relational data model fits the domain perfectly: meetings have participants, transcripts, and action items — all with clear foreign key relationships
- Used MySQL in previous projects (Stickify, ParkEase) — zero learning curve
- `ddl-auto: update` auto-creates and updates tables from entity classes — fast iteration during development
- Strong support in Spring Data JPA with minimal configuration

### Alternatives considered
- **PostgreSQL** — more feature-rich (JSONB, full-text search), free on Render. Could be a better production choice
- **MongoDB** — flexible schema would suit the transcript and analysis JSON storage. However, relational integrity for meetings ↔ action items ↔ reminder logs is better expressed in SQL
- **SQLite** — zero setup, good for prototyping, but not suitable for deployment on cloud platforms

### Trade-offs
- MySQL requires a running server (vs SQLite's file-based approach)
- `analysisJson` and `transcriptJson` are stored as `LONGTEXT` — not queryable as structured data. Acceptable because analysis content is only read/displayed, not filtered
- PostgreSQL would be preferred in production for its native JSON querying support

---

## 3. Authentication — JWT (Stateless)

### Decision
Use **JWT (JSON Web Tokens)** with **Spring Security** for stateless authentication.

### Why chosen
- Stateless — no server-side session storage needed. Each request is self-contained
- Works well with REST APIs — token is passed in `Authorization: Bearer` header
- Scales horizontally without session synchronization between instances
- jjwt library is mature, well-documented, and integrates cleanly with Spring Security
- 24-hour expiry balances security and convenience for evaluation

### Alternatives considered
- **Session-based auth** — simpler to implement, but requires server-side state. Doesn't scale well and complicates deployment
- **OAuth2** — industry standard for third-party auth, but overly complex for this assignment scope. No third-party identity provider needed
- **API Key** — simplest option, but offers no user-level isolation or expiry

### Trade-offs
- JWTs cannot be invalidated before expiry (no logout mechanism)
- Token size is larger than a session ID
- Acceptable for this assignment — the assignment only requires "authentication for protected APIs" without specifying logout

---

## 4. LLM Provider — Groq (LLaMA3-8b)

### Decision
Use **Groq API** with the **LLaMA3-8b-8192** model for AI meeting analysis.

### Why chosen
- **Free tier** — no billing setup or credit card required. Critical for a time-boxed assignment
- **Fast** — Groq's hardware (LPUs) delivers significantly faster inference than OpenAI. Low latency for analysis endpoint
- **OpenAI-compatible API** — same request/response format as OpenAI. Switching to OpenAI or Claude in future requires changing only the base URL and model name
- LLaMA3-8b is capable enough for structured JSON extraction tasks with good prompt engineering

### Alternatives considered
- **OpenAI GPT-4o** — best quality, but requires billing. Not suitable for free evaluation
- **Google Gemini** — free tier available, but API format differs from OpenAI standard
- **Anthropic Claude** — excellent reasoning, but paid API
- **OpenRouter** — aggregates many models, free credits available, but adds an extra network hop

### Trade-offs
- LLaMA3-8b is less capable than GPT-4o for complex reasoning
- Mitigated by: strict system prompt, `temperature: 0.1` (low = deterministic), required JSON schema in prompt, JSON validation before saving
- Free tier has rate limits — not suitable for high-throughput production use

---

## 5. External Integration — Telegram Bot API

### Decision
Use **Telegram Bot API** for sending overdue action item reminders.

### Why chosen
- Completely free — no billing, no credit card
- Simple REST API — sending a message is a single HTTP POST call
- Setup takes under 5 minutes via @BotFather
- No SDK needed — raw HTTP call with WebClient is sufficient
- Reliable delivery — Telegram has excellent uptime

### Alternatives considered
- **Slack Webhook** — easy to set up, but requires a Slack workspace with admin access
- **SendGrid Email** — more professional for reminders, but requires domain verification and has free tier limits
- **Discord Webhook** — similar simplicity to Telegram, but less commonly used in professional context
- **Notion API** — interesting but overkill for simple reminder notifications

### Trade-offs
- Requires the recipient to have a Telegram account
- Chat ID must be manually obtained (not a seamless user experience)
- Acceptable for this assignment — the requirement is just "one real third-party integration actively used by the reminder workflow"

---

## 6. Project Structure — Layered Architecture with Service Interface Pattern

### Decision
Use a standard **layered architecture**: Controller → Service (Interface + Impl) → Repository → Entity, with separate DTOs for requests and responses.

### Why chosen
- Clear separation of concerns — each layer has a single responsibility
- Controllers handle HTTP only, services handle business logic, repositories handle DB
- DTOs decouple the API contract from the database schema — internal model changes don't break API responses
- Interface + Impl pattern (e.g. `MeetingService` + `MeetingServiceImpl`) follows Spring best practices and makes unit testing with mocks straightforward

### Alternatives considered
- **Anemic model** (all logic in controllers) — faster to write but impossible to test and maintain
- **Domain-Driven Design** — appropriate for large systems, but overkill for this scope
- **Single service class without interface** — simpler, but less extensible

### Trade-offs
- More files and folders than a simple single-layer approach
- Slightly more code per feature (interface + impl)
- Justified because it directly maps to what evaluators look for: "clean architecture, maintainable code"

---

## 7. Transcript Storage — Separate `Transcript` Table

### Decision
Store each transcript entry as a **separate row** in a `transcripts` table linked to the meeting by foreign key.

### Why chosen
- Proper relational modeling — each entry (timestamp, speaker, text) is structured data, not a blob
- Allows querying individual transcript entries if needed
- Clean ORM mapping with `@OneToMany` on Meeting entity
- More maintainable than storing transcript as a JSON string

### Alternatives considered
- **Single LONGTEXT JSON column** — simpler schema, one field on Meeting. Loses queryability and type safety
- **Separate transcript service** — unnecessary abstraction at this scale

### Trade-offs
- More complex schema vs. a single JSON column
- Multiple DB inserts per meeting creation (one per transcript entry) vs. one
- Justified because structured storage is better engineering practice and aligns with the assignment's emphasis on database design quality

---

## 8. Analysis Storage — JSON String on Meeting Table

### Decision
Store the full LLM analysis response as a **`LONGTEXT` JSON string** (`analysisJson`) directly on the `Meeting` entity.

### Why chosen
- Analysis is returned as a nested JSON object (summary, decisions, followUpSuggestions arrays) — mapping this to relational tables would require 3+ additional join tables
- Analysis content is always read together, never queried individually
- Action items (the queryable part) are extracted separately and stored in the `action_items` table
- Keeps schema simple while preserving full analysis fidelity

### Alternatives considered
- **Full relational mapping** — separate tables for summary, decisions, follow-ups. Adds complexity with no query benefit for this scope
- **PostgreSQL JSONB** — best of both worlds (stored as JSON, queryable). Not available in MySQL without workarounds

### Trade-offs
- `analysisJson` field is not queryable by content
- Acceptable because only action items need to be queried — the rest is display-only

---

## 9. Scheduled Reminders — Spring `@Scheduled`

### Decision
Use **Spring's built-in `@Scheduled` annotation** for the reminder cron job.

### Why chosen
- Zero additional dependencies — built into Spring Boot with `@EnableScheduling`
- Simple to configure — one annotation with a cron expression
- Sufficient for this assignment's requirement of a background job that detects overdue items and sends reminders
- `reminderSent` flag on `ActionItem` prevents duplicate notifications across scheduler runs

### Alternatives considered
- **Quartz Scheduler** — powerful, supports clustering and job persistence, but major overkill for a single cron job
- **External job queue (Redis/RabbitMQ)** — appropriate for distributed systems, unnecessary here

### Trade-offs
- `@Scheduled` runs in-process — if the app restarts during a scheduled run, the job doesn't retry
- Does not persist job history natively (handled by `ReminderLog` table instead)
- Acceptable for this assignment scope

---

## Summary Table

| Decision | Chosen | Key Reason |
|----------|--------|-----------|
| Language | Java 17 | Primary language, no learning curve |
| Framework | Spring Boot 3.2.5 | All features built-in (security, scheduling, JPA) |
| Database | MySQL | Relational model fits domain, prior experience |
| Auth | JWT Stateless | REST-compatible, scalable, no session state |
| LLM | Groq (LLaMA3) | Free, fast, OpenAI-compatible API |
| Notifications | Telegram Bot | Free, simple REST API, 5-min setup |
| Architecture | Layered (Controller/Service/Repository) | Clean, testable, maintainable |
| Transcript storage | Separate table | Proper relational modeling |
| Analysis storage | JSON string column | Avoids unnecessary schema complexity |
| Scheduler | Spring @Scheduled | Zero dependencies, sufficient for scope |
