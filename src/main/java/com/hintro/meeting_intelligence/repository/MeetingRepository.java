package com.hintro.meeting_intelligence.repository;

import com.hintro.meeting_intelligence.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>{

}
