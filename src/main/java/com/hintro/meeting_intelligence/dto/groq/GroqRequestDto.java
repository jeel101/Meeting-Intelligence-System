package com.hintro.meeting_intelligence.dto.groq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroqRequestDto {
    private String model;

    private List<MessageDto> messages;
}
