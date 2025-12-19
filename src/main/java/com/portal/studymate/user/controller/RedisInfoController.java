package com.portal.studymate.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisInfoController {

   private final RedisConnectionFactory redisConnectionFactory;
   @GetMapping("/info")
   public ResponseEntity<String> getRedisInfo() {

      try{  // Cast to LettuceConnectionFactory to access host, port, database
            if (!(redisConnectionFactory instanceof LettuceConnectionFactory lettuceFactory)) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                              .body("RedisConnectionFactory is not LettuceConnectionFactory");
      }

      String host = lettuceFactory.getHostName();
      int port = lettuceFactory.getPort();
      int db = lettuceFactory.getDatabase();

      // Ping Redis
      try (RedisConnection connection = redisConnectionFactory.getConnection()) {
         String ping = connection.ping();
         String message = String.format(
            "Redis connection details:\nHost: %s\nPort: %d\nDatabase: %d\nPing response: %s\nStatus: %s",
            host, port, db, ping, (ping != null ? "CONNECTED" : "DISCONNECTED")
         );
         return ResponseEntity.ok(message);
      }
   } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Redis connection failed: " + e.getMessage());
   }

   }
}
