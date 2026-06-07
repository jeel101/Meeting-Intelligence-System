package com.hintro.meeting_intelligence.repository;

import com.hintro.meeting_intelligence.entity.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
    List<MeetingParticipant> findByMeetingId(Long meetingId);

    boolean existsByMeetingIdAndUserId( Long meetingId,Long userId);
}