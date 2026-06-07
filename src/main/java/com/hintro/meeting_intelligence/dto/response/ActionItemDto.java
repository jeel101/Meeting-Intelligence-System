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
public class ActionItemDto {
    private String task;

    private String assignee;

    private String dueDate;

    private List<CitationDto> citations;
}
