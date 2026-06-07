package com.hintro.meeting_intelligence.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TranscriptRequestDto {
    @NotBlank(message = "Timestamp is required")
    private String timestamp;

    @NotBlank(message = "Speaker is required")
    private String speaker;

    @NotBlank(message = "Transcript text is required")
    private String text;
}
