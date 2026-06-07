package com.hintro.meeting_intelligence.repository;

import com.hintro.meeting_intelligence.entity.Meeting;
import com.hintro.meeting_intelligence.entity.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Long>{
    List<Transcript> findByMeetingIdOrderByIdAsc(Long meetingId);
}
