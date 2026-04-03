package com.portal.studymate.grading.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "grading_scale_entry", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"grading_scale_id", "grade_name"})
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class GradingScaleEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grading_scale_id", nullable = false)
    private GradingScale gradingScale;

    @Column(name = "grade_name", nullable = false, length = 10)
    private String gradeName;

    @Column(name = "min_percentage", nullable = false)
    private BigDecimal minPercentage;

    @Column(name = "max_percentage", nullable = false)
    private BigDecimal maxPercentage;

    @Column(name = "grade_point")
    private BigDecimal gradePoint;

    private String description;
}
