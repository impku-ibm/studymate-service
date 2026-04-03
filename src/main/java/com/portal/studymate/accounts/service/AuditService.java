package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.enums.AuditAction;

public interface AuditService {
   void logAction(AuditAction action, String entityType, Long entityId, String details);
}