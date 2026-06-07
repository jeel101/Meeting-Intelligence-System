package com.hintro.meeting_intelligence.service;

import com.hintro.meeting_intelligence.dto.response.ActionItemResponseDto;
import com.hintro.meeting_intelligence.entity.ActionItemStatus;

import java.util.List;

public interface ActionItemService {
    ActionItemResponseDto updateStatus(
            Long actionItemId,
            ActionItemStatus status);

    List<ActionItemResponseDto> getActionItems(ActionItemStatus status,String assignee,Long meetingId);

    List<ActionItemResponseDto> getOverdueActionItems();
}
