package com.hintro.meeting_intelligence.controller;

import com.hintro.meeting_intelligence.dto.response.AnalysisResponseDto;
import com.hintro.meeting_intelligence.dto.response.ApiResponseDto;
import com.hintro.meeting_intelligence.logging.TraceFilter;
import com.hintro.meeting_intelligence.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;
    @PostMapping("/{id}/analyze")
    public ResponseEntity<ApiResponseDto<AnalysisResponseDto>> analyzeMeeting(@PathVariable Long id) {

        return ResponseEntity.ok(ApiResponseDto.success(
                        analysisService.analyzeMeeting(id),
                        MDC.get(TraceFilter.TRACE_ID_MDC_KEY)
                )
        );
    }
}
