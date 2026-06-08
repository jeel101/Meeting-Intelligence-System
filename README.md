# Meeting Intelligence Service

AI-powered backend service that helps users manage meetings, extract actionable insights from transcripts, track action items, and send automated reminders.

Built for the **Hintro Backend/Fullstack Engineering Internship Assignment**.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Features](#features)
- [Setup Instructions](#setup-instructions)
- [Environment Variables](#environment-variables)
- [Local Execution Steps](#local-execution-steps)
- [Deployment Instructions](#deployment-instructions)
- [API Usage Examples](#api-usage-examples)
- [API Documentation](#api-documentation)

---

## Tech Stack

| Layer         | Technology                         |
| ------------- | ---------------------------------- |
| Language      | Java 17                            |
| Framework     | Spring Boot 3.4.3                  |
| Database      | MySQL 8                            |
| ORM           | Spring Data JPA + Hibernate        |
| Auth          | JWT (jjwt 0.12.6)                  |
| LLM           | Groq API (llama-3.3-70b-versatile) |
| Notifications | Telegram Bot API                   |
| API Docs      | Springdoc OpenAPI (Swagger)        |
| Build Tool    | Maven                              |

---

## Features

- JWT Authentication (register/login)
- Create meetings with full transcripts
- AI-powered meeting analysis — summary, action items, decisions, follow-up suggestions
- Every AI insight is grounded with transcript citations (timestamp references)
- Action item management with status tracking (PENDING → IN_PROGRESS → COMPLETED)
- Overdue action item detection
- Scheduled Telegram reminders for overdue items
- Unified API response format with trace IDs on every request
- Global error handling — never crashes on bad input
- Input validation on all endpoints
- Swagger UI for live API testing

---

## Setup Instructions

### Prerequisites

Make sure the following are installed:

- Java 17+ → `java -version`
- Maven 3.8+ → `mvn -version`
- MySQL 8+ running locally

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/meeting-intelligence.git
cd meeting-intelligence
```

### 2. Create the database

```sql
CREATE DATABASE meeting_intelligence;
```

### 3. Get required API keys

**Groq API (free LLM):**

1. Go to https://console.groq.com
2. Sign up and create an API key
3. Copy the key

**Telegram Bot:**

1. Open Telegram → search `@BotFather`
2. Send `/newbot` and follow prompts
3. Copy the bot token
4. Start a chat with your bot, then visit:
   `https://api.telegram.org/bot<YOUR_TOKEN>/getUpdates`
5. Copy the `chat.id` from the response

---

## Environment Variables

Create a `.env` file in the project root or set these in your IDE run configuration:

```env
# Database
DB_PASSWORD=your_mysql_password

# JWT
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

# Groq LLM
GROQ_API_KEY=your_groq_api_key

# Telegram
TELEGRAM_BOT_TOKEN=your_telegram_bot_token
TELEGRAM_CHAT_ID=your_telegram_chat_id
```

> **Note:** Never commit real API keys to GitHub. Add `.env` to your `.gitignore`.

### All variables reference in `application.yml`:

| Variable             | Description                        | Required |
| -------------------- | ---------------------------------- | -------- |
| `DB_PASSWORD`        | MySQL password                     | Yes      |
| `JWT_SECRET`         | 256-bit hex string for signing JWT | Yes      |
| `GROQ_API_KEY`       | API key from console.groq.com      | Yes      |
| `TELEGRAM_BOT_TOKEN` | Token from @BotFather              | Yes      |
| `TELEGRAM_CHAT_ID`   | Chat ID to send reminders to       | Yes      |

---

## Local Execution Steps

### 1. Install dependencies

```bash
mvn clean install -DskipTests
```

### 2. Run the application

```bash
mvn spring-boot:run
```

Or with environment variables set inline:

```bash
DB_PASSWORD=yourpass GROQ_API_KEY=yourkey TELEGRAM_BOT_TOKEN=yourtoken TELEGRAM_CHAT_ID=yourid mvn spring-boot:run
```

### 3. Verify it's running

```bash
curl http://localhost:8080/health
# Expected: {"status":"UP"}
```

### 4. Open Swagger UI

Visit: http://localhost:8080/swagger-ui.html

Click **Authorize**, paste your JWT token, and test all endpoints.

---

## Deployment Instructions

### Deploy to Render (recommended — free tier)

#### 1. Push to GitHub

```bash
git add .
git commit -m "initial commit"
git push origin main
```

#### 2. Create Render account

Go to https://render.com and sign up.

#### 3. New Web Service

- Click **New → Web Service**
- Connect your GitHub repo
- Set these values:

| Field         | Value                                                      |
| ------------- | ---------------------------------------------------------- |
| Runtime       | Java                                                       |
| Build Command | `mvn clean install -DskipTests`                            |
| Start Command | `java -jar target/meeting-intelligence-0.0.1-SNAPSHOT.jar` |
| Instance Type | Free                                                       |

#### 4. Add environment variables in Render dashboard

Add all variables from the Environment Variables section above.

Also add:

```
PORT=8080
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

#### 5. Add MySQL

- Go to **New → PostgreSQL** (or use Railway for MySQL)
- Copy the connection URL and update `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`

#### 6. Update `SystemController.java`

After deployment, update these with your actual URLs:

```java
"repositoryUrl", "https://github.com/YOUR_USERNAME/meeting-intelligence"
"deployedUrl",   "https://your-app.onrender.com"
```

---

## API Usage Examples

### Authentication

**Register:**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jeel Shah",
    "email": "jeel@example.com",
    "password": "password123"
  }'
```

**Login:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jeel@example.com",
    "password": "password123"
  }'
```

Copy the `token` from the response and use it as `Bearer <token>` in all subsequent requests.

---

### Meetings

**Create a meeting:**

```bash
curl -X POST http://localhost:8080/api/meetings \
  -H "Authorization: Bearer <your_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Sprint Planning",
    "participants": ["alice@example.com", "bob@example.com"],
    "meetingDate": "2026-05-20T10:00:00",
    "transcript": [
      {"timestamp": "00:10", "speaker": "John", "text": "We should launch next Friday."},
      {"timestamp": "00:20", "speaker": "Alice", "text": "I will prepare the release notes."},
      {"timestamp": "00:35", "speaker": "Bob", "text": "I will handle the deployment pipeline."}
    ]
  }'
```

**Get a meeting:**

```bash
curl http://localhost:8080/api/meetings/1 \
  -H "Authorization: Bearer <your_token>"
```

**List meetings (paginated):**

```bash
curl "http://localhost:8080/api/meetings?page=0&size=10" \
  -H "Authorization: Bearer <your_token>"
```

**Trigger AI analysis:**

```bash
curl -X POST http://localhost:8080/api/meetings/1/analyze \
  -H "Authorization: Bearer <your_token>"
```

---

### Action Items

**Get all action items for a meeting:**

```bash
curl "http://localhost:8080/api/action-items?meetingId=1" \
  -H "Authorization: Bearer <your_token>"
```

**Filter by status:**

```bash
curl "http://localhost:8080/api/action-items?status=PENDING" \
  -H "Authorization: Bearer <your_token>"
```

**Filter by assignee:**

```bash
curl "http://localhost:8080/api/action-items?assignee=Alice" \
  -H "Authorization: Bearer <your_token>"
```

**Update status:**

```bash
curl -X PATCH http://localhost:8080/api/action-items/1/status \
  -H "Authorization: Bearer <your_token>" \
  -H "Content-Type: application/json" \
  -d '{"status": "IN_PROGRESS"}'
```

**Get overdue items:**

```bash
curl http://localhost:8080/api/action-items/overdue \
  -H "Authorization: Bearer <your_token>"
```

---

### System

```bash
curl http://localhost:8080/health
curl http://localhost:8080/api/evaluation
```

---

## API Documentation

Swagger UI is available at:

- **Local:** http://localhost:8080/swagger-ui.html
- **Deployed:** https://your-app.onrender.com/swagger-ui.html
