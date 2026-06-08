package com.hintro.meeting_intelligence.dto.response;

import com.hintro.meeting_intelligence.dto.ActionItemDto;
import com.hintro.meeting_intelligence.dto.InsightDto;
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
    private List<InsightDto> summary;

    private List<InsightDto> decisions;

    private List<InsightDto> followUps;

    private List<ActionItemDto> actionItems;
}
