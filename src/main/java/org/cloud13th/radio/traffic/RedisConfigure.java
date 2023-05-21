package org.cloud13th.radio.traffic;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Data
@Configuration
public class RedisConfigure {

    private final LettuceConnectionFactory factory;

    @Bean
    RedisTemplate<String, Long> redisTemplate() {
        var template = new RedisTemplate<String, Long>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(StringRedisSerializer.UTF_8);
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }

    @Bean
    RedisScript<Boolean> redisScript() {
        return RedisScript.of(new ClassPathResource("scripts/rateLimiter.lua"), Boolean.class);
    }
}
