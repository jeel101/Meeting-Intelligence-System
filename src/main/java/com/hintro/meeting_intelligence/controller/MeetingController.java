package com.hintro.meeting_intelligence.controller;

import com.hintro.meeting_intelligence.dto.request.MeetingRequestDto;
import com.hintro.meeting_intelligence.dto.response.MeetingDetailResponse;
import com.hintro.meeting_intelligence.dto.response.MeetingResponseDto;
import com.hintro.meeting_intelligence.dto.response.ApiResponseDto;
import com.hintro.meeting_intelligence.logging.TraceFilter;
import com.hintro.meeting_intelligence.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/meeting")
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping("create-meet")
    public ResponseEntity<ApiResponseDto<MeetingResponseDto>> createMeeting(
            @Valid @RequestBody MeetingRequestDto request) {

        return ResponseEntity.ok(ApiResponseDto.success(
                        meetingService.createMeeting(request),
                        MDC.get(TraceFilter .TRACE_ID_MDC_KEY)
                )
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<MeetingDetailResponse>> getMeeting(@PathVariable Long id) {

        return ResponseEntity.ok(ApiResponseDto.success(
                        meetingService.getMeeting(id),
                        MDC.get(TraceFilter.TRACE_ID_MDC_KEY)
                )
        );
    }
    @GetMapping
    @Operation(summary = "List all meetings — paginated")
    public ResponseEntity<ApiResponseDto<Page<MeetingResponseDto>>> getAllMeetings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<MeetingResponseDto> meetings = meetingService.getAllMeetings(page, size);

        return ResponseEntity.ok(
                ApiResponseDto.success(meetings, MDC.get(TraceFilter.TRACE_ID_MDC_KEY))
        );
    }
}
