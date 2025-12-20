package com.portal.studymate.user.repository;

import com.portal.studymate.user.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchoolRepository extends JpaRepository<School, UUID> {
}
