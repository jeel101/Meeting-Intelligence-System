package com.hintro.meeting_intelligence.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private ReminderChannel channel;

    @Enumerated(EnumType.STRING)
    private ReminderStatus status;

    @Column(columnDefinition = "TEXT")
    private String response;

    private LocalDateTime sentAt;
}
