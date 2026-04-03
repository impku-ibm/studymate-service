package com.portal.studymate.accounts.model;

import com.portal.studymate.accounts.enums.FeeFrequency;
import com.portal.studymate.accounts.enums.FeeType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fee_plan_item", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"fee_plan_id", "fee_type"})
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class FeePlanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_plan_id", nullable = false)
    private FeePlan feePlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_type", nullable = false)
    private FeeType feeType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeFrequency frequency = FeeFrequency.MONTHLY;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
