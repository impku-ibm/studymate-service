package com.portal.studymate.accounts.model;

import com.portal.studymate.accounts.enums.DiscountType;
import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.school.model.School;
import com.portal.studymate.student.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fee_discount")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    private BigDecimal percentage;

    @Column(name = "fixed_amount")
    private BigDecimal fixedAmount;

    private String reason;

    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}