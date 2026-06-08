# AI_APPROACH.md

## Model Used
**Groq API** — model: `llama-3.3-70b-versatile`
Chosen for: free tier, fast inference, strong JSON instruction-following, OpenAI-compatible API.

---

## Prompt Design
Single user prompt containing:
- Expected JSON output schema with citations on every field
- Strict grounding rules
- Today's date (for resolving relative due dates)
- Full transcript formatted as `[timestamp] Speaker: text`

Rules are placed **before** the transcript so the model reads constraints first.

---

## Citation Strategy
Citations are required on **every insight** — not just action items. Summary points, decisions, and follow-ups all carry at least one citation referencing the exact transcript timestamp where that insight was derived.

Output schema enforced in prompt:
```json
{
  "summary":    [{ "text": "...", "citations": [{ "timestamp": "00:30" }] }],
  "decisions":  [{ "text": "...", "citations": [{ "timestamp": "02:45" }] }],
  "followUps":  [{ "text": "...", "citations": [{ "timestamp": "07:45" }] }],
  "actionItems":[{ "task": "...", "assignee": "...", "dueDate": "YYYY-MM-DD", "citations": [{ "timestamp": "04:50" }] }]
}
```

Action item citations stored as JSON string (`citations_json`) in the `action_items` table.

---

## Due Date Parsing
Today's date is injected into the prompt so the model can resolve relative dates:
- `"by Monday"` → `2026-06-09`
- `"15th June"` → `2026-06-15`
- `"end of next week"` → resolved to actual date

Model returns `dueDate` as `YYYY-MM-DD` or `null` if not mentioned.

---

## Hallucination Prevention
1. **Explicit rules in prompt** — NEVER invent attendees, action items, or decisions not in transcript
2. **Null for missing data** — assignee and dueDate return `null` if not mentioned, not guessed
3. **Structured schema** — model must fit output into a rigid JSON structure, no free-form text
4. **Citation required on everything** — every summary, decision, follow-up and action item must have at least one citation, making it impossible to generate insights without grounding them in the transcript
5. **Re-analyze blocked** — a meeting can only be analyzed once, preventing result drift
6. **Omit over guess** — prompt instructs: *"If something is unclear, omit it — do not guess"*
7. **Empty arrays over omission** — decisions, followUps, actionItems return `[]` if none found

---

## Output Validation
- Markdown fences stripped before parsing (`\`\`\`json` blocks removed)
- Response parsed with Jackson — invalid JSON logged and returns `400 BAD_REQUEST`
- Typed `AnalysisResponseDto` acts as schema validator — missing/wrong fields fail deserialization
- Empty action items list handled gracefully — no crash, loop simply doesn't execute

---

## Known Limitations
- Citations not cross-validated against actual transcript timestamps
- No retry on LLM failure — rate limits or timeouts require manual re-call
- Relative due dates depend on LLM interpretation and may occasionally be inaccurate
