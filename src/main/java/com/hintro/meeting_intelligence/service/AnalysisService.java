package com.hintro.meeting_intelligence.service;

import com.hintro.meeting_intelligence.dto.response.AnalysisResponseDto;

public interface AnalysisService {
    AnalysisResponseDto analyzeMeeting(Long meetingId);
}
