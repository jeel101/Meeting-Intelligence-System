package com.hintro.meeting_intelligence.service;

import com.hintro.meeting_intelligence.dto.request.MeetingRequestDto;
import com.hintro.meeting_intelligence.dto.response.MeetingDetailResponse;
import com.hintro.meeting_intelligence.dto.response.MeetingResponseDto;

import java.util.List;

public interface MeetingService {
    MeetingResponseDto createMeeting(MeetingRequestDto request);

    MeetingDetailResponse getMeeting(Long id);

    List<MeetingResponseDto> getAllMeetings();
}
