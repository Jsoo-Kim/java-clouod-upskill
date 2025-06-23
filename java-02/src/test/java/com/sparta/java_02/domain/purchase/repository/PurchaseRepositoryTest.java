package com.sparta.java_02.domain.purchase.repository;

import com.sparta.java_02.common.enums.PurchaseStatus;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional  // DB에 쌓이지 않는다?
@SpringBootTest
public class PurchaseRepositoryTest {

  @Autowired
  private PurchaseRepository purchaseRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void 저장() {

    User user = userRepository.findById(1L).get();

    Purchase purchase = Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();
    purchaseRepository.save(purchase);

    List<Purchase> purchases = new ArrayList<>();
    purchaseRepository.saveAll(purchases);

  }

  @Test
  void 수정() {
    User user = userRepository.save(User.builder()
        .name("d")
        .email("d")
        .passwordHash("d")
        .build());

    Purchase purchase = Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase = purchaseRepository.save(purchase);

    savePurchase.setStatus(PurchaseStatus.COMPLETION);
    purchaseRepository.save(savePurchase);
  }

  @Test
  void 삭제() {

  }

  @Test
  void 조회() {
//    Purchase purchase = purchaseRepository.findById(1L)
//        .orElseThrow(() -> new RuntimeException("주문 내역이 없음"));
//
//    System.out.println("결과: " + purchase.getTotalPrice());

//    userRepository.findAllByWithPurchases();  // Fetch Join으로 N+1 해결
  }


}

