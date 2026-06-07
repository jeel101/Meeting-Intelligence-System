package com.hintro.meeting_intelligence.service.impl;

import com.hintro.meeting_intelligence.dto.response.ActionItemResponseDto;
import com.hintro.meeting_intelligence.entity.ActionItem;
import com.hintro.meeting_intelligence.entity.ActionItemStatus;
import com.hintro.meeting_intelligence.exception.AppException;
import com.hintro.meeting_intelligence.repository.ActionItemRepository;
import com.hintro.meeting_intelligence.service.ActionItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionItemServiceImpl implements ActionItemService {
    private final ActionItemRepository actionItemRepository;
    @Override
    public ActionItemResponseDto updateStatus(Long actionItemId, ActionItemStatus status) {
        ActionItem actionItem =actionItemRepository
                        .findById(actionItemId)
                        .orElseThrow(() -> AppException.notFound("Action item not found"));

        actionItem.setStatus(status);
        actionItem = actionItemRepository.save(actionItem);

        return mapToDto(actionItem);
    }
    @Override
    public List<ActionItemResponseDto> getActionItems(ActionItemStatus status, String assignee, Long meetingId) {
        List<ActionItem> actionItems;

        if(status != null) {
            actionItems =actionItemRepository.findByStatus(status);
        } else if(assignee != null) {
            actionItems =actionItemRepository.findByAssignee(assignee);
        } else if(meetingId != null) {
            actionItems = actionItemRepository.findByMeetingId(meetingId);
        } else {
            actionItems = actionItemRepository.findAll();
        }

        return actionItems.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<ActionItemResponseDto> getOverdueActionItems() {
        return actionItemRepository.findByStatusNotAndDueDateBefore(
                        ActionItemStatus.COMPLETED,
                        LocalDate.now()
                ).stream().map(this::mapToDto).toList();
    }

    private ActionItemResponseDto mapToDto(ActionItem actionItem) {

        return ActionItemResponseDto.builder()
                .id(actionItem.getId())
                .task(actionItem.getTask())
                .assignee(actionItem.getAssignee())
                .status(actionItem.getStatus())
                .dueDate(actionItem.getDueDate())
                .meetingId(
                        actionItem.getMeeting().getId()
                )
                .build();
    }
}
