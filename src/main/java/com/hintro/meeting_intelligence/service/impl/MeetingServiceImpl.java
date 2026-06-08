package com.hintro.meeting_intelligence.service.impl;

import com.hintro.meeting_intelligence.dto.request.MeetingRequestDto;
import com.hintro.meeting_intelligence.dto.request.TranscriptRequestDto;
import com.hintro.meeting_intelligence.dto.response.MeetingDetailResponse;
import com.hintro.meeting_intelligence.dto.response.MeetingResponseDto;
import com.hintro.meeting_intelligence.dto.response.TranscriptResponseDto;
import com.hintro.meeting_intelligence.entity.Meeting;
import com.hintro.meeting_intelligence.entity.MeetingParticipant;
import com.hintro.meeting_intelligence.entity.Transcript;
import com.hintro.meeting_intelligence.entity.User;
import com.hintro.meeting_intelligence.exception.AppException;
import com.hintro.meeting_intelligence.repository.MeetingRepository;
import com.hintro.meeting_intelligence.repository.ParticipantRepository;
import com.hintro.meeting_intelligence.repository.TranscriptRepository;
import com.hintro.meeting_intelligence.repository.UserRepository;
import com.hintro.meeting_intelligence.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final ParticipantRepository ParticipantRepository;
    private final TranscriptRepository transcriptRepository;
    private final UserRepository userRepository;
    @Override
    public MeetingResponseDto createMeeting(MeetingRequestDto request) {
        Meeting meeting = new Meeting();

        meeting.setTitle(request.getTitle());
        meeting.setMeetingDate(request.getMeetingDate());

        meeting = meetingRepository.save(meeting);


        // participants
        for (String email : request.getParticipants()) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->AppException.notFound("User not found : " + email)
                    );

            MeetingParticipant participant =  new MeetingParticipant();

            participant.setMeeting(meeting);
            participant.setUser(user);
            ParticipantRepository.save(participant);
        }

        // transcripts
        for (TranscriptRequestDto transcriptDto: request.getTranscript()) {

            Transcript transcript = new Transcript();

            transcript.setMeeting(meeting);
            transcript.setTimestamp(transcriptDto.getTimestamp());
            transcript.setSpeaker(transcriptDto.getSpeaker());
            transcript.setText( transcriptDto.getText());

            transcriptRepository.save(transcript);
        }

        return mapToDto(meeting);
    }

    @Override
    public MeetingDetailResponse getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() ->AppException.notFound("Meeting not found"));

        return mapToDetailDto(meeting);
    }

//    @Override
//    public List<MeetingResponseDto> getAllMeetings() {
//        return meetingRepository.findAll()
//                .stream()
//                .map(this::mapToDto)
//                .toList();
//    }

    @Override
    public Page<MeetingResponseDto> getAllMeetings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return meetingRepository.findAll(pageable).map(this::mapToDto);
    }

    private MeetingResponseDto mapToDto(Meeting meeting) {

        return MeetingResponseDto.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .meetingDate( meeting.getMeetingDate())
                .createdAt(meeting.getCreatedAt())
                .build();
    }

    private MeetingDetailResponse mapToDetailDto(Meeting meeting) {
        return MeetingDetailResponse.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .meetingDate( meeting.getMeetingDate())

                .participants(meeting.getParticipants()
                        .stream()
                        .map(participant ->
                                participant.getUser().getEmail())
                        .toList())

                .transcript(meeting.getTranscrips()
                        .stream()
                        .map(t ->
                                TranscriptResponseDto.builder()
                                        .timestamp(t.getTimestamp())
                                        .speaker(t.getSpeaker())
                                        .text(t.getText())
                                        .build()
                        ).toList())
                .createdAt(meeting.getCreatedAt())
                .build();
    }
}
