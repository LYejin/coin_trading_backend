package com.project.coinTrade.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD) // 메서드에만 적용
@Retention(RetentionPolicy.RUNTIME) // 런타임에도 유지
@Documented
public @interface LogExecution {
    boolean logParams() default true;   // 파라미터 로깅 여부
    boolean logReturn() default true;   // 리턴값 로깅 여부
    boolean measureTime() default true; // 실행 시간 측정 여부
}
