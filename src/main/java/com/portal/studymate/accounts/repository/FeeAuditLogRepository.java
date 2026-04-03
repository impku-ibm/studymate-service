package com.portal.studymate.accounts.repository;

import com.portal.studymate.accounts.model.FeeAuditLog;
import com.portal.studymate.school.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeAuditLogRepository extends JpaRepository<FeeAuditLog, Long> {
   List<FeeAuditLog> findBySchoolOrderByPerformedAtDesc(School school);
}