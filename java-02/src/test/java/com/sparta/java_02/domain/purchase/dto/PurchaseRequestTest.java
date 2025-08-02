package com.sparta.java_02.domain.purchase.dto;

import java.util.List;

public class PurchaseRequestTest {

  Long userId;

  private List<PurchaseProductRequestTest> products;

  public PurchaseRequestTest(Long userId, List<PurchaseProductRequestTest> products) {
    this.userId = userId;
    this.products = products;
  }

  public Long getUserId() {
    return userId;
  }

  public List<PurchaseProductRequestTest> getProducts() {
    return products;
  }
}
