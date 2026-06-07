package com.hintro.meeting_intelligence.repository;

import com.hintro.meeting_intelligence.entity.MeetingAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisRepository extends JpaRepository<MeetingAnalysis, Long> {
    boolean existsByMeetingId(Long meetingId);
}
