package com.portal.studymate.staff.model;

import com.portal.studymate.school.model.School;
import com.portal.studymate.staff.enums.StaffType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"school_id", "email"})
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_type", nullable = false)
    private StaffType staffType;

    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
