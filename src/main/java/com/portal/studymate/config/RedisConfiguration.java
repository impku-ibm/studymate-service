package com.portal.studymate.config;

import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

   @Bean
   public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
      RedisTemplate<String, String> template = new RedisTemplate<>();
      template.setConnectionFactory(factory);

      template.setKeySerializer(new StringRedisSerializer());
      template.setValueSerializer(new StringRedisSerializer());
      template.setHashKeySerializer(new StringRedisSerializer());
      template.setHashValueSerializer(new StringRedisSerializer());

      template.afterPropertiesSet();
      return template;
   }

   @Bean
   public LettuceClientConfigurationBuilderCustomizer lettuceCustomizer() {
      return builder -> builder
                           .useSsl()
                           .disablePeerVerification();
   }
}
