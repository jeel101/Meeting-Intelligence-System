package com.hintro.meeting_intelligence.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeetingRequestDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Meeting date is required")
    private LocalDateTime meetingDate;

    @NotEmpty(message = "At least one participant is required")
    private List<String> participants;

    @NotEmpty(message = "Transcript cannot be empty")
    private List<TranscriptRequestDto> transcript;
}
