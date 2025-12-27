package com.portal.studymate.common.jwt;

import com.portal.studymate.schoolmodule.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

   @Value("${jwt.secret}")
   private String secret;

   @Value("${jwt.expiration}")
   private long expiration;

   private Key signingKey;

   @PostConstruct
   public void init() {
      if (secret == null || secret.length() < 32) {
         throw new IllegalStateException("JWT secret must be at least 256 bits");
      }
      this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
   }

   /* ================= ACCESS TOKEN ================= */

   public String generateToken(User user) {
      return Jwts.builder()
                 .setSubject(user.getId())
                 .setIssuer("studymate")
                 .claim("email", user.getEmail())
                 .claim("role", user.getRole().name())
                 .claim("schoolId", user.getSchoolId())
                 .setIssuedAt(new Date())
                 .setExpiration(new Date(System.currentTimeMillis() + expiration))
                 .signWith(signingKey)
                 .compact();
   }

   public Claims parseToken(String token) {
      return Jwts.parserBuilder()
                 .setSigningKey(signingKey)
                 .build()
                 .parseClaimsJws(token)
                 .getBody();
   }

   public long getRemainingValidity(String token) {
      Claims claims = parseToken(token);
      Date expiration = claims.getExpiration();
      return expiration.getTime() - System.currentTimeMillis();
   }

   public String generateRefreshToken(User user) {
      long expiry = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7);

      return Jwts.builder()
                 .setSubject(user.getId())
                 .setIssuer("studymate")
                 .claim("schoolId", user.getSchoolId())
                 .setIssuedAt(new Date())
                 .setExpiration(new Date(expiry))
                 .signWith(signingKey)
                 .compact();
   }
}
