package com.sparta.java_02.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/*
 @AllArgsConstructor, @Getter 등을 붙인 건 향후 필드를 추가했을 때 번거로운 보일러플레이트(생성자·getter)를 자동으로 생성해 주기 위함이다.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum TaskStatus {
  PENDING,
  PROCESSING,
  COMPLETED,
  ;
}
