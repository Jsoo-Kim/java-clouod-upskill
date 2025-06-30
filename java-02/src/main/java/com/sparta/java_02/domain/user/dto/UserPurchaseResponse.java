package com.sparta.java_02.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPurchaseResponse {

  Long id;

  String name;

  String email;

  Long purchaseId;

  BigDecimal purchaseTotalPrice;

  @QueryProjection  // QUserPurchaseProjection 파일을 만들어줌!
  public UserPurchaseResponse(Long id, String name, String email, Long purchaseId,
      BigDecimal purchaseTotalPrice) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.purchaseId = purchaseId;
    this.purchaseTotalPrice = purchaseTotalPrice;
  }
}
