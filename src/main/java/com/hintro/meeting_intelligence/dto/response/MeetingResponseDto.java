package com.hintro.meeting_intelligence.dto.response;

import com.hintro.meeting_intelligence.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
