package com.portal.studymate.accounts.model;

import com.portal.studymate.accounts.enums.AuditAction;
import com.portal.studymate.school.model.School;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fee_audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeAuditLog {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "school_id", nullable = false)
   private School school;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private AuditAction action;

   @Column(nullable = false)
   private String entityType;

   @Column(nullable = false)
   private Long entityId;

   @Column(columnDefinition = "TEXT")
   private String details;

   @Column(name = "performed_by")
   private String performedBy;

   @Column(name = "performed_at", updatable = false)
   private LocalDateTime performedAt = LocalDateTime.now();
}