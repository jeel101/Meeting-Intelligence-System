package com.hintro.meeting_intelligence.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reminder_logs")
public class ReminderLog extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_log_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_item_id")
    private ActionItem actionItem;

    @Enumerated(EnumType.STRING)
    private ReminderChannel channel;

    @Enumerated(EnumType.STRING)
    private ReminderStatus status;

    private String reminder;

    private String assignee;

    private LocalDate dueDate;

    private LocalDateTime sentAt;
}
