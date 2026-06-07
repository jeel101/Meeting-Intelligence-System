package com.hintro.meeting_intelligence.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "meeting_analysis")
public class MeetingAnalysis extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", unique = true)
    private Meeting meeting;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String analysisJson;

    private  LocalDateTime generatedAt;
}
