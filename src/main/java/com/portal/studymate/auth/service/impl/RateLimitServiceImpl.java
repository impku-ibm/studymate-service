package com.portal.studymate.auth.service.impl;

import com.portal.studymate.auth.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimitServiceImpl implements RateLimitService {

   private final StringRedisTemplate redisTemplate;

   @Override
   public boolean isAllowed(String key, int maxRequests, Duration ttl) {
      try {
         Long count = redisTemplate.opsForValue().increment(key);
         if (count != null && count == 1) {
            redisTemplate.expire(key, ttl);
         }
         return count != null && count <= maxRequests;
      } catch (Exception e) {
         log.warn("Redis unavailable for rate limiting, allowing request");
         return true;
      }
   }
}
