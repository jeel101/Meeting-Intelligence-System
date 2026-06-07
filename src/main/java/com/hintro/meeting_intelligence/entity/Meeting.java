package com.hintro.meeting_intelligence.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "meetings")
public class Meeting extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id", nullable = false)
    private Long id;

    private String title;

    private LocalDateTime meetingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @OneToMany(mappedBy = "meeting")
    private List<MeetingParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "meeting")
    private List<Transcript> transcrips = new ArrayList<>();

    @OneToMany(mappedBy = "meeting")
    private List<ActionItem> actionItems = new ArrayList<>();
}
