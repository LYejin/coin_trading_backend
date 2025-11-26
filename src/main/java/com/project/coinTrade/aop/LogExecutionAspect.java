package com.project.coinTrade.aop;

import com.project.coinTrade.annotation.LogExecution;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class LogExecutionAspect {

    private static final Logger log = LoggerFactory.getLogger(LogExecutionAspect.class);

    @Around("@annotation(logExecution)")
    public Object logMethod(ProceedingJoinPoint joinPoint, LogExecution logExecution) throws Throwable {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // Controller라면 HTTP 요청 정보 가져오기
        HttpServletRequest request = null;
        if(RequestContextHolder.getRequestAttributes() != null){
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }

        // 시작 로그
        if (logExecution.logParams()) {
            String params = Arrays.toString(args);
            if(request != null){
                log.info("▶ Start: {}.{}() | URL={} | Method={} | params={}",
                        className, methodName, request.getRequestURI(), request.getMethod(), params);
            } else {
                log.info("▶ Start: {}.{}() | params={}", className, methodName, params);
            }
        }

        long start = System.currentTimeMillis();

        Object result = null;
        try {
            result = joinPoint.proceed(); // 실제 메서드 호출
        } catch (Throwable e) {
            log.error("Exception in {}.{}() | cause={}", className, methodName, e.getMessage(), e);
            throw e; // 예외는 그대로 다시 던짐
        }

        long end = System.currentTimeMillis();
        long elapsed = end - start;

        // 종료 로그
        if (logExecution.logReturn()) {
            log.info("◀ End: {}.{}() | result={} | {} ms", className, methodName, result, elapsed);
        } else if (logExecution.measureTime()) {
            log.info("◀ End: {}.{}() | {} ms", className, methodName, elapsed);
        }

        return result;
    }
}
