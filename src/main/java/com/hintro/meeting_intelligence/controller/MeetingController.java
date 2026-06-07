package com.hintro.meeting_intelligence.controller;

import com.hintro.meeting_intelligence.dto.request.MeetingRequestDto;
import com.hintro.meeting_intelligence.dto.response.MeetingDetailResponse;
import com.hintro.meeting_intelligence.dto.response.MeetingResponseDto;
import com.hintro.meeting_intelligence.dto.response.ApiResponseDto;
import com.hintro.meeting_intelligence.logging.TraceFilter;
import com.hintro.meeting_intelligence.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
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
    @GetMapping("get-all")
    public ResponseEntity<ApiResponseDto<List<MeetingResponseDto>>>getAllMeetings() {

        return ResponseEntity.ok(
                ApiResponseDto.success(meetingService.getAllMeetings(),
                        MDC.get(TraceFilter.TRACE_ID_MDC_KEY)
                )
        );
    }
}
