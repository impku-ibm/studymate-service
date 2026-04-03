package com.portal.studymate.timetable.model;

import com.portal.studymate.school.model.School;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "period_definition", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"school_id", "period_number"})
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PeriodDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "period_number", nullable = false)
    private Integer periodNumber;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_break")
    private boolean isBreak;

    private String label;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
