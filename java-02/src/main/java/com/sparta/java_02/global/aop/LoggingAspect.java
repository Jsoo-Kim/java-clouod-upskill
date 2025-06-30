package com.sparta.java_02.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect  // 스프링 프레임워크가 아니라서 @Component를 내장하고 있지 않음
@Component  // 스프링 컨테이너에 등록!
public class LoggingAspect {

  // 1. execution: `controller` 패키지 내의 모든 메서드 실행 전에 적용
  @Before("execution(* com.sparta.java_02.domain..controller..*(..))")
  public void logBeforeApiExecution() {
    log.info("[API-execution] API 메서드 실행 전 로그");
  }

  // 2. within: `domain` 패키지 내의 모든 메서드 실행 전에 적용
  @Before("within(com.sparta.java_02.domain..*)")  // 시그니처 지정 X
  public void logBeforeWithin() {
    log.info("[within] domain 패키지 내부 메서드 실행 전 로그");
  }

  // 3. @annotation: @Loggable 어노테이션이 붙은 메서드 실행 전에만 적용
  @Before("@annotation(com.sparta.java_02.common.annotation.Loggable)")
  public void logBeforeAnnotation() { // 어노테이션의 기능은 aop에 작성하는 방식!
    log.info("[@annotation] @Loggable 어노테이션 적용된 메서드 실행 전 로그");
  }

  // 4. JoinPoint 활용: 메서드의 상세 정보 로깅
  @Before("execution(* com.sparta.java_02.domain..*(..))")
  public void logMethodDetails(JoinPoint joinPoint) {
    log.info("실행된 메서드 이름: {}", joinPoint.getSignature().getName());
    Object[] args = joinPoint.getArgs();
    if (args.length > 0) {
      log.info("전달된 파라미터: {}", args);
    }
  }
}
