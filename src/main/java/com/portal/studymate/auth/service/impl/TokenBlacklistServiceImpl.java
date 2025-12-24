package com.portal.studymate.auth.service.impl;

import com.portal.studymate.auth.service.TokenBlacklistService;
import com.portal.studymate.common.util.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

   private final RedisTemplate<String, String> redisTemplate;

   public void blacklistToken(String token, long expirationMillis) {
      redisTemplate.opsForValue().set(
         "blacklist:access:" + HashUtil.sha256(token),
         "true",
         expirationMillis,
         TimeUnit.MILLISECONDS
      );
   }

   public boolean isTokenBlacklisted(String token) {
      return Boolean.TRUE.equals(
         redisTemplate.hasKey("blacklist:access:" + HashUtil.sha256(token))
      );
   }
}
