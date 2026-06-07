package com.hintro.meeting_intelligence.dto;

import com.hintro.meeting_intelligence.dto.response.CitationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionItemDto {
    private String task;

    private String assignee;

    private LocalDate dueDate;

    private List<CitationDto> citations;
}
