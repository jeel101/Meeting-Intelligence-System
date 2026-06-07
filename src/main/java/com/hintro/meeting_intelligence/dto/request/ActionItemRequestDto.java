package com.hintro.meeting_intelligence.dto.request;

import com.hintro.meeting_intelligence.entity.ActionItemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActionItemRequestDto {
    @NotNull(message = "Status is required")
    private ActionItemStatus status;
}
