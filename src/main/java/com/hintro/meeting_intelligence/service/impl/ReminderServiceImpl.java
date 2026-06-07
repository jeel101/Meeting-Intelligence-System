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
            String message = "Reminder\n\n"
                            + "Task: " + item.getTask()
                            + "\nAssignee: "
                            + item.getAssignee()
                            + "\nDue Date: "
                            + item.getDueDate();

            String response = telegramService.sendReminder(message);
            ReminderLog reminderLog = new ReminderLog();
            try {
                reminderLog.setStatus(ReminderStatus.SENT);
                reminderLog.setResponse(response);
                reminderLog.setActionItem(item);
                reminderLog.setSentAt(LocalDateTime.now());
                reminderLog.setMessage(message);
                reminderLog.setChannel(ReminderChannel.TELEGRAM);
            }catch (Exception e) {
                reminderLog.setStatus(ReminderStatus.FAILED);
                reminderLog.setResponse(e.getMessage());
            }

            reminderLogRepository.save(reminderLog);
        }
    }
}
