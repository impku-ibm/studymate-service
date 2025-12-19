package com.portal.studymate.auth.service;

import java.time.Duration;

public interface RateLimitService {
   boolean isAllowed(String key, int maxRequests, Duration ttl);
}
