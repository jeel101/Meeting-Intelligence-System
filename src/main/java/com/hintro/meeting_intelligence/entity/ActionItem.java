package com.hintro.meeting_intelligence.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "action_items")
public class ActionItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_item_id", nullable = false)
    private Long id;

    private String task;

    private String assignee;

    @Enumerated(EnumType.STRING)
    private ActionItemStatus status;

    private LocalDate dueDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String citationsJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @OneToMany(mappedBy = "actionItem")
    private List<ReminderLog> reminderLogs = new ArrayList<>();
}
