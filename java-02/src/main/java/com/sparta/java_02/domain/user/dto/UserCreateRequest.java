package com.sparta.java_02.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {  // Jackson이 이걸 객체로 변환해줌!

  @NotBlank
  String name;

  @Email
  String email;

  @Pattern(regexp = "^\\d{10}$", message = "Password must be 10 digits")
  String passwordHash;

}
