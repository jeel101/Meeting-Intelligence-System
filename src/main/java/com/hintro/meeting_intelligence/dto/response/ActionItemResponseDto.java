package com.hintro.meeting_intelligence.dto.response;

import com.hintro.meeting_intelligence.entity.ActionItemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionItemResponseDto {
    private Long id;

    private String task;

    private String assignee;

    private ActionItemStatus status;

    private LocalDate dueDate;

    private Long meetingId;
}
