package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final UserRepository userRepository;
  private final PurchaseRepository purchaseRepository;

//  public final Purchase getPurchase(Long purchseId) {
//    return purchaseRepository.findById(purchseId)
//        .orElseThrow(
//            () -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));
//  }

  @Transactional
  public void placePurchase() {

  }

}
