package com.hintro.meeting_intelligence.controller;

import com.hintro.meeting_intelligence.dto.request.ActionItemRequestDto;
import com.hintro.meeting_intelligence.dto.response.ActionItemResponseDto;
import com.hintro.meeting_intelligence.dto.response.ApiResponseDto;
import com.hintro.meeting_intelligence.entity.ActionItemStatus;
import com.hintro.meeting_intelligence.logging.TraceFilter;
import com.hintro.meeting_intelligence.service.ActionItemService;
import com.hintro.meeting_intelligence.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/action-items")
public class ActionItemController {
    private final ActionItemService actionItemService;
    private final ReminderService reminderService;

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponseDto<ActionItemResponseDto>> updateStatus(
            @PathVariable Long id,@Valid @RequestBody ActionItemRequestDto request) {

        return ResponseEntity.ok(ApiResponseDto.success(actionItemService
                                .updateStatus(id, request.getStatus()),
                        MDC.get(TraceFilter.TRACE_ID_MDC_KEY)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<ActionItemResponseDto>>> getActionItems(
            @RequestParam(required = false) ActionItemStatus status, @RequestParam(required = false)
            String assignee, @RequestParam(required = false) Long meetingId) {

        return ResponseEntity.ok(ApiResponseDto.success(actionItemService
                                .getActionItems(status,assignee,meetingId),
                        MDC.get( TraceFilter.TRACE_ID_MDC_KEY)
                )
        );
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponseDto<List<ActionItemResponseDto>>>getOverdueActionItems() {

        return ResponseEntity.ok(ApiResponseDto.success(actionItemService.getOverdueActionItems(),
                        MDC.get(TraceFilter.TRACE_ID_MDC_KEY)
                )
        );
    }

//    @PostMapping("/trigger-reminders")public ResponseEntity<String> triggerReminders() {
//        reminderService.processOverdueItems();
//        return ResponseEntity.ok("Reminders sent");
//    }

}
