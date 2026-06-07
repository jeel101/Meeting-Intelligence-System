package com.hintro.meeting_intelligence.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponseDto {
    private String summary;

    private List<String> decisions;

    private List<String> followUps;

    private List<ActionItemDto> actionItems;
}
