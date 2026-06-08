# TESTING.md

All testing was performed manually via Postman against the locally running application.

---

## Test Scenarios Executed

### Authentication

| Scenario | Request | Expected | Result |
|----------|---------|----------|--------|
| User Registration | POST /api/auth/register | User registered, token returned | ✅ PASS |
| User Login | POST /api/auth/login | JWT token returned | ✅ PASS |

### Meeting Management

| Scenario | Request | Expected | Result |
|----------|---------|----------|--------|
| Create Meeting | POST /api/meetings | Meeting created with participants and transcript | ✅ PASS |
| Get Meeting By ID | GET /api/meetings/{id} | Meeting details returned | ✅ PASS |
| Get All Meetings | GET /api/meetings | Paginated list returned | ✅ PASS |

### AI Analysis

| Scenario | Request | Expected | Result |
|----------|---------|----------|--------|
| Analyze Meeting | POST /api/meetings/{id}/analyze | Summary, decisions, follow-ups, action items generated with citations | ✅ PASS |
| Re-analyze blocked | POST /api/meetings/{id}/analyze (2nd call) | 409 CONFLICT returned | ✅ PASS |

### Action Item Management

| Scenario | Request | Expected | Result |
|----------|---------|----------|--------|
| Get All Action Items | GET /api/action-items | All action items returned | ✅ PASS |
| Filter by Status | GET /api/action-items?status=PENDING | Only matching items returned | ✅ PASS |
| Filter by Assignee | GET /api/action-items?assignee={name} | Only matching items returned | ✅ PASS |
| Filter by Meeting ID | GET /api/action-items?meetingId={id} | Only matching items returned | ✅ PASS |
| Update Status | PATCH /api/action-items/{id}/status | Status updated successfully | ✅ PASS |

### Overdue Detection

| Scenario | Request | Expected | Result |
|----------|---------|----------|--------|
| Get Overdue Items | GET /api/action-items/overdue | Items where status != COMPLETED and dueDate < now | ✅ PASS |
| No overdue items | GET /api/action-items/overdue (none exist) | Empty list returned, no error | ✅ PASS |

### Reminder Workflow

| Scenario | Expected | Result |
|----------|----------|--------|
| Scheduler execution | Overdue items identified, Telegram message sent | ✅ PASS |
| Reminder log creation | Reminder history stored in reminder_logs table | ✅ PASS |
| Duplicate reminder prevention | reminderSent flag prevents re-sending on next scheduler run | ✅ PASS |

### System Endpoints

| Scenario | Request | Expected | Result |
|----------|---------|----------|--------|
| Health Check | GET /health | `{"status": "UP"}` | ✅ PASS |
| Evaluation Endpoint | GET /api/evaluation | Assignment metadata returned | ✅ PASS |

---

## Edge Cases Considered

1. Invalid login credentials → 401 UNAUTHORIZED returned
2. Missing JWT token on protected endpoint → 403 returned
3. Invalid/expired JWT token → 403 returned
4. Meeting not found → 404 NOT_FOUND returned
5. Action item not found → 404 NOT_FOUND returned
6. Meeting analyzed more than once → 409 CONFLICT returned
7. Empty or missing required fields → 400 VALIDATION_ERROR with field-level message
8. Invalid status value in PATCH request → 400 BAD_REQUEST returned
9. No overdue action items exist → empty list returned, no crash
10. Scheduler running repeatedly → reminderSent flag prevents duplicate Telegram messages
11. Invalid email format in participants list → 400 VALIDATION_ERROR returned
12. LLM returns malformed JSON → 400 BAD_REQUEST returned gracefully

---

## Limitations Discovered

1. AI responses may vary slightly between executions despite low temperature setting
2. Reminder notifications depend on Telegram API availability — no retry on failure
3. Due dates mentioned relationally in transcript (e.g. "next Friday") depend on LLM interpretation and may be inaccurate
4. No markdown stripping on LLM response — if model wraps JSON in code fences, parsing fails
5. Citation timestamps returned by LLM are not cross-validated against actual transcript timestamps
6. Reminder scheduling frequency is fixed via cron expression in configuration
