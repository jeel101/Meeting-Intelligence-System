package com.hintro.meeting_intelligence.dto.response;

import com.hintro.meeting_intelligence.dto.ActionItemDto;
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
    private List<String> summary;

    private List<String> decisions;

    private List<String> followUps;

    private List<ActionItemDto> actionItems;
}
