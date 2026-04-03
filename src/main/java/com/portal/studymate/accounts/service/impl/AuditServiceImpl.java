package com.portal.studymate.accounts.service.impl;

import com.portal.studymate.accounts.enums.AuditAction;
import com.portal.studymate.accounts.model.FeeAuditLog;
import com.portal.studymate.accounts.repository.FeeAuditLogRepository;
import com.portal.studymate.accounts.service.AuditService;
import com.portal.studymate.common.context.SchoolContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

   private final FeeAuditLogRepository auditLogRepository;

   @Override
   public void logAction(AuditAction action, String entityType, Long entityId, String details) {
      try {
         FeeAuditLog auditLog = new FeeAuditLog();
         auditLog.setSchool(SchoolContext.getSchool());
         auditLog.setAction(action);
         auditLog.setEntityType(entityType);
         auditLog.setEntityId(entityId);
         auditLog.setDetails(details);
         auditLog.setPerformedBy(SecurityContextHolder.getContext().getAuthentication().getName());
         auditLogRepository.save(auditLog);
      } catch (Exception e) {
         // Don't let audit logging break the main operation
         log.warn("Audit log failed: {}", e.getMessage());
      }
   }
}