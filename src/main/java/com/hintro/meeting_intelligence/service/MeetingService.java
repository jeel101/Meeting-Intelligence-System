package com.hintro.meeting_intelligence.service;

import com.hintro.meeting_intelligence.dto.request.MeetingRequestDto;
import com.hintro.meeting_intelligence.dto.response.MeetingDetailResponse;
import com.hintro.meeting_intelligence.dto.response.MeetingResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MeetingService {
    MeetingResponseDto createMeeting(MeetingRequestDto request);

    MeetingDetailResponse getMeeting(Long id);

    Page<MeetingResponseDto> getAllMeetings(int page, int size);
}
