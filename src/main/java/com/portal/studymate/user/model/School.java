package com.portal.studymate.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schools")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class School extends BaseEntity {
   @Column(nullable = false)
   private String name;

   @Column(nullable = false)
   private String board;

   private String address;
}
