package com.sparta.java_02.global.aop;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

  // 서울(한국) 타임존 상수
  private static final ZoneId SEOUL_ZONE = ZoneId.of("Aisa/Seoul");
  // 포맷팅
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss.SSS");

  // Pointcut: Service 계층의 모든 메서드를 대상으로 지정
  @Pointcut("execution(* com.sparta.java_02.domain..service..*(..))")
  private void allServiceMethods() {
  }

  @Around("allServiceMethods()")
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    // 1. 메서드 실행 전: 시작 시간 기록
    long startMillis = System.currentTimeMillis();
    String startTime = Instant.ofEpochMilli(startMillis).atZone(SEOUL_ZONE).format(FORMATTER);

    log.info("메서드 시작 시간 : {}", startTime);

    // 2. 실제 타겟 메서드 실행
    Object result = joinPoint.proceed();

    // 3. 메서드 실행 후: 종료 시간 기록 및 실행 시간 계산/로깅
    long endMillis = System.currentTimeMillis();
    String endTime = Instant.ofEpochMilli(endMillis).atZone(SEOUL_ZONE).format(FORMATTER);
    long executionTime = endMillis - startMillis;

    log.info("메서드 종료 시간: {}", endTime);
    log.info("'{}' 메서드 실행 시간: {}ms", joinPoint.getSignature().toShortString(), executionTime);

    // 4. 원래 메서드의 실행 결과를 반환
    return result;
  }

}
