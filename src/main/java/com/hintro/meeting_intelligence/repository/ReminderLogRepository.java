package com.hintro.meeting_intelligence.repository;

import com.hintro.meeting_intelligence.entity.ReminderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderLogRepository extends JpaRepository<ReminderLog, Long> {

}
