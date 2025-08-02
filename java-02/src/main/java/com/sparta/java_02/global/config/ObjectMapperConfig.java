package com.sparta.java_02.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

  @Bean
  public ObjectMapper objectMapper() {
    /*
     ① 기본 생성자만 사용할 경우,
       Java 8 이상의 java.time API 타입(LocalDate, LocalDateTime 등)에 대한 직렬화/역직렬화 모듈이 등록되지 않아
       오류가 나거나 날짜가 null 처리가 될 수 있음
    */
//    return new ObjectMapper();

    /*
     ② ObjectMapper 인스턴스 생성
     */
    ObjectMapper objectMapper = new ObjectMapper();

    /*
     ③ JavaTimeModule 등록
       - Jackson이 java.time.* 타입(LocalDate, LocalDateTime 등)을 읽고 쓸 수 있게 해 주는 모듈
       - 이 모듈이 없으면 LocalDateTime 같은 필드가 직렬화되지 않거나 역직렬화 시 예외 발생
       - 이제 Feign이 보내고 받는 DTO에 날짜·시간 프로퍼티가 있더라도 안전하게 매핑됨
    */
    objectMapper.registerModule(new JavaTimeModule());

    /*
     ④ WRITE_DATES_AS_TIMESTAMPS 비활성화
       - 기본값은 날짜/시간을 long 타입(에포크 밀리초)으로 직렬화하는 것
       - 비활성화하면 ISO-8601 문자열 ("2025-08-02T14:30:00") 형태로 출력돼,
         클라이언트(Feign)와의 JSON 포맷이 맞춰져 호환성이 보장됨

       Jackson의 기본 동작은 날짜/시간을 에포크(epoch) 숫자로 출력하는 것인데, (예: 1690965000000)
       Feign을 쓰는 REST API에서는 사람이 읽기 좋은 ISO-8601 문자열 포맷이 더 흔히 쓰임 (예:"2025-08-02T14:30:00")
    */
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /*
     ⑤ 이렇게 설정한 ObjectMapper를 빈으로 등록
       - 이제 Spring MVC, Feign, RestTemplate 등 Jackson을 쓰는 모든 컴포넌트가 동일한 설정을 공유
     */
    return objectMapper;
  }
}
