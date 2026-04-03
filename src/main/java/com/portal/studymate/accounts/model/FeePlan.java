package com.portal.studymate.accounts.model;

import com.portal.studymate.school.model.School;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fee_plan", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"school_id", "name"})
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class FeePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(nullable = false)
    private String name;

    private String description;

    private boolean active = true;

    @OneToMany(mappedBy = "feePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeePlanItem> items;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
