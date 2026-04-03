package com.portal.studymate.timetable.model;

import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.model.Subject;
import com.portal.studymate.teachermgmt.model.Teacher;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "timetable_entry", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"class_id", "section", "period_definition_id", "day_of_week"})
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class TimetableEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_definition_id", nullable = false)
    private PeriodDefinition periodDefinition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private SchoolClass schoolClass;

    @Column(nullable = false, length = 10)
    private String section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
