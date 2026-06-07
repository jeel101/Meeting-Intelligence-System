package com.hintro.meeting_intelligence.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptResponseDto {
    private String timestamp;

    private String speaker;

    private String text;
}
