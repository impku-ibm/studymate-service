package com.portal.studymate.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity {
   @Id
   @GeneratedValue
   @Column(columnDefinition = "uuid")
   protected UUID id;

   @Column(name = "created_at", updatable = false)
   protected Instant createdAt = Instant.now();
}
