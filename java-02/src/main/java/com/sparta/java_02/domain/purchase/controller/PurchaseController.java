package com.sparta.java_02.domain.purchase.controller;


import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequest;
import com.sparta.java_02.domain.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class PurchaseController {

  private final PurchaseService purchaseService;

  @PostMapping
  public ApiResponse<Void> save(@Valid @RequestBody PurchaseRequest request) {
    purchaseService.purchase(request);
    return ApiResponse.success();
  }

}
