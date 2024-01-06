package com.xab.stock.annotation;


import com.xab.stock.domain.constant.Constants;
import com.xab.stock.enums.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author template
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     */
    String key() default Constants.RATE_LIMIT_KEY;

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;
}
