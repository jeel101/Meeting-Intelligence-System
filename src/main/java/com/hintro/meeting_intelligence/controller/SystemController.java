package com.hintro.meeting_intelligence.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class SystemController {
    @GetMapping("/health")
    @Operation(summary = "Health check — returns UP if service is running")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/evaluation")
    @Operation(summary = "Evaluation info — for Hintro evaluators")
    public Map<String, Object> evaluation() {
        return Map.of(
                "candidateName", "Jeel Shah",
                "email", "jeelshah408@gmail.com",
                "repositoryUrl", "https://github.com/jeel101/Meeting-Intelligence-System",
                "deployedUrl", "NA",
                "externalIntegration", "Telegram Bot API",
                "features", List.of(
                        "JWT Authentication",
                        "Meeting Management with Transcripts",
                        "AI Analysis with Grounded Citations (Groq LLaMA3)",
                        "Action Item Management",
                        "Overdue Detection",
                        "Scheduled Telegram Reminder",
                        "Unified API Response Format",
                        "Request Trace ID",
                        "Global Error Handling",
                        "Input Validation",
                        "Swagger Documentation"
                )
        );
    }
}
