package com.hintro.meeting_intelligence.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDetailResponse {
    private Long id;
    private String title;
    private LocalDateTime meetingDate;
    private List<String> participants;
    private List<TranscriptResponseDto> transcript;
    private LocalDateTime createdAt;
}