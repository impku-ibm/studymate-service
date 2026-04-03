package com.portal.studymate.grading.model;

import com.portal.studymate.school.model.School;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "grading_scale", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"school_id", "name"})
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class GradingScale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_default")
    private boolean isDefault;

    private boolean active = true;

    @OneToMany(mappedBy = "gradingScale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GradingScaleEntry> entries;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
