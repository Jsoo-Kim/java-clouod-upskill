package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelRequest;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelResponse;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequest;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final PurchaseCancelService cancelService;
  private final PurchaseProcessService purchaseProcessService;

  private final UserRepository userRepository;
  private final PurchaseRepository purchaseRepository;


  @Transactional
  public void createPurchase(PurchaseRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    purchaseProcessService.process(user, request.getPurchaseItems());
  }

//  @Transactional
//  public void cancel(Long purchaseId) {
//    Purchase purchase = purchaseRepository.findById(purchaseId)
//        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PURCHASE));
//
//    // 서비스 클래스에서 상태를 직접 변경
//    if (purchase.getStatus() != PurchaseStatus.PENDING) {
//      throw new ServiceException(ServiceExceptionCode.CANNOT_CANCEL);
//    }
//
//    purchase.setStatus(PurchaseStatus.CANCELED);
//  }

  @Transactional
  public PurchaseCancelResponse cancelPurchase(PurchaseCancelRequest request) {
    return cancelService.cancel(request.getPurchaseId(), request.getUserId());
  }

}
