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
