package com.hintro.meeting_intelligence.schedular;

import com.hintro.meeting_intelligence.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {
    private final ReminderService reminderService;

    @Scheduled(fixedRate = 300000)
    public void sendReminders() {
        reminderService.processOverdueItems();
    }
}
