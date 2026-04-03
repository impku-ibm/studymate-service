package com.portal.studymate.student.model;

import com.portal.studymate.school.model.School;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_certificate", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"school_id", "tc_number"}),
    @UniqueConstraint(columnNames = {"student_id"})
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class TransferCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    @Column(name = "tc_number", nullable = false)
    private String tcNumber;

    @Column(name = "leaving_date", nullable = false)
    private LocalDate leavingDate;

    @Column(name = "reason_for_leaving")
    private String reasonForLeaving;

    @Column(length = 50)
    private String conduct = "GOOD";

    private String remarks;

    @Column(name = "generated_by")
    private String generatedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
