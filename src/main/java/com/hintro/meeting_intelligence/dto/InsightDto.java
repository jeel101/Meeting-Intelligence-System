package com.hintro.meeting_intelligence.dto;

import com.hintro.meeting_intelligence.dto.response.CitationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsightDto {
    private List<String> text;

    private List<CitationDto> citations;
}
