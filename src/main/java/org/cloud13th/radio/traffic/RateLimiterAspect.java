package org.cloud13th.radio.traffic;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Aspect
@Component
public class RateLimiterAspect {

    private final RedisTemplate<String, Long> redisTemplate;
    private final RedisScript<Boolean> redisScript;

    public RateLimiterAspect(RedisTemplate<String, Long> redisTemplate, RedisScript<Boolean> redisScript) {
        this.redisTemplate = redisTemplate;
        this.redisScript = redisScript;
    }

    @Pointcut(value = "@annotation(org.cloud13th.radio.traffic.RateLimiter)")
    public void rateLimiterPoint() {
    }

    @Around(value = "rateLimiterPoint() && @annotation(rateLimiter)", argNames = "rateLimiter")
    public Object rateLimiterHandler(ProceedingJoinPoint joinPoint, RateLimiter rateLimiter) throws Throwable {
        var method = joinPoint.getSignature().getName();
        var key = String.format("R_L:%s:%s", method, LocalTime.now().getMinute());
        log.info("\nRateLimiter >>> {}\nConfigure >>> {}", key, String.format("%s/%s req/s", rateLimiter.maxRequests(), rateLimiter.limitDuration()));
        Boolean execute = redisTemplate.execute(redisScript, List.of(key), rateLimiter.maxRequests(), rateLimiter.limitDuration());
        return Boolean.TRUE.equals(execute)
                ? ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build()
                : joinPoint.proceed(joinPoint.getArgs());
    }
}
