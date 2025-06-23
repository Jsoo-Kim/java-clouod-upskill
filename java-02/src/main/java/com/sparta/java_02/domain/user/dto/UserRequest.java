package com.sparta.java_02.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {  // Jackson이 이걸 객체로 변환해줌!

  String name;
  String email;
  String password;

}
