package com.hintro.meeting_intelligence.repository;

import com.hintro.meeting_intelligence.entity.ActionItem;
import com.hintro.meeting_intelligence.entity.ActionItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
    List<ActionItem> findByStatus(ActionItemStatus status);

    List<ActionItem> findByAssignee(String assignee);

    List<ActionItem> findByMeetingId(Long meetingId);

    List<ActionItem> findByStatusNotAndDueDateBefore(
            ActionItemStatus status,
            LocalDate date);
}
