package org.cloud13th.radio.traffic;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Inherited
public @interface RateLimiter {

    long maxRequests() default 20;

    long limitDuration() default 60;
}
