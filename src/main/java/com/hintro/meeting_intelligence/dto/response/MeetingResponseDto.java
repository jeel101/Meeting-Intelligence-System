package com.hintro.meeting_intelligence.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingResponseDto {
    private Long id;

    private String title;

    private LocalDateTime meetingDate;

    private LocalDateTime createdAt;
}
