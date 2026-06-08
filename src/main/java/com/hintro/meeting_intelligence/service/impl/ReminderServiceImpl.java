package com.hintro.meeting_intelligence.service.impl;

import com.hintro.meeting_intelligence.entity.*;
import com.hintro.meeting_intelligence.repository.ActionItemRepository;
import com.hintro.meeting_intelligence.repository.ReminderLogRepository;
import com.hintro.meeting_intelligence.service.ReminderService;
import com.hintro.meeting_intelligence.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {
    private final ActionItemRepository actionItemRepository;
    private final ReminderLogRepository reminderLogRepository;
    private final TelegramService telegramService;

    @Override
    public void processOverdueItems() {
        List<ActionItem> overdueItems = actionItemRepository
                .findByStatusNotAndDueDateBefore(ActionItemStatus.COMPLETED, LocalDate.now());

        for(ActionItem item : overdueItems) {
            boolean alreadySent = reminderLogRepository
                    .existsByActionItemIdAndStatus(item.getId(), ReminderStatus.SENT);

            if (alreadySent) {
                continue;
            }

            String message = "Reminder: " + item.getTask()
                            + "\n\nAssigned To: " + item.getAssignee()
                            + "\n\nDue Date: " + item.getDueDate();

            ReminderLog reminderLog = new ReminderLog();

            reminderLog.setActionItem(item);
            reminderLog.setSentAt(LocalDateTime.now());
            reminderLog.setChannel(ReminderChannel.TELEGRAM);
            reminderLog.setReminder(item.getTask());
            reminderLog.setAssignee(item.getAssignee());
            reminderLog.setDueDate(item.getDueDate());

            try {
                telegramService.sendReminder(message);
                reminderLog.setStatus( ReminderStatus.SENT);
            } catch (Exception e) {
                reminderLog.setStatus(ReminderStatus.FAILED);
            }

            reminderLogRepository.save(
                    reminderLog
            );
        }
    }
}
