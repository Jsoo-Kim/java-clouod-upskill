package com.sparta.java_02.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor // 왜 필요하지?
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceException extends RuntimeException { // RuntimeException이 광범위한 Exception이라서 사용함

  String code;
  String message;

  public ServiceException(ServiceExceptionCode code) {
    super(code.getMessage());  // RuntimeException의 생성자가 문자열을 요구함
    this.code = code.name();
    this.message = super.getMessage();
  }

  @Override
  public String getMessage() {
    return message;
  }
}

/*
 [기본 생성자가 필요해지는 경우]
 1. 프레임워크 프로시(proxy) 생성
   - Spring AOP, CGLIB, Hibernate 같은 라이브러리들이 런타임에 프록시 객체를 만들 때
   - “파라미터 없는 생성자”를 호출해서 빈 인스턴스를 찍어낸 뒤, 내부 필드를 채워 넣는 방식으로 동작한다.
 2. 직렬화/역직렬화
   - Jackson, Gson 같은 JSON 매퍼가 예외 객체를 역직렬화할 때
   - 기본 생성자로 객체를 먼저 만들고, setter나 리플렉션으로 필드를 채운다.
 3. 코드 어딘가에서 new ServiceException()을 직접 호출
   - 파라미터 없이 그냥 던지고 싶은 경우가 있을 수 있다.

 [Lombok @NoArgsConstructor의 역할]
 - 위 상황들에서 쓰일 public ServiceException() 생성자를 자동으로 만들어 준다.
 - (별도로 super()도 호출해서 RuntimeException 쪽 기본 생성자가 실행된다.)
 */
