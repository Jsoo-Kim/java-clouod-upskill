package com.sparta.java_02.domain.product.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToCartRequest {

  String command;

}
