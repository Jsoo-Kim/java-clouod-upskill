package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.common.enums.PurchaseStatus;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequest;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.entity.PurchaseProduct;
import com.sparta.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.task.service.TaskQueueService;
import com.sparta.java_02.domain.user.entity.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseProcessService {

  private final TaskQueueService taskQueueService;
  private final PurchaseRepository purchaseRepository;
  private final ProductRepository productRepository;
  private final PurchaseProductRepository purchaseProductRepository;

  @Async  // ① 호출한 스레드와 분리된 스레드 풀에서 비동기로 실행
  @Transactional(propagation = Propagation.REQUIRED)  // ② 트랜잭션 안에서 실행
  public void purchaseProcess(Long taskQueueId, PurchaseRequest request, User user) {

    // ③ 작업 큐 상태 전환 흐름 관리:
    //    1) 대기(PENDING) → ‘처리 중(PROCESSING)’으로 표시
    //    2) 람다로 실제 주문 처리 로직 실행
    //    3) 작업 완료 후 ‘완료(COMPLETED)’으로 표시
    taskQueueService.processQueueById(taskQueueId, (taskQueue) -> {

      // ④ 사용자 정보 기반으로 주문 생성·저장
      Purchase purchase = createAndSavePurchase(user);

      // ⑤ 큐에 생성된 주문 ID 기록
      taskQueue.setEventId(purchase.getId());

      // ⑥ 주문 상품 처리(재고 차감 등)
      List<PurchaseProduct> purchaseProducts = createAndProcessPurchaseProducts(
          request.getProducts(),
          purchase);

      // ⑦ 최종 결제 금액 계산 후 주문 객체에 설정
      BigDecimal totalPrice = calculateTotalPrice(purchaseProducts);
      purchase.setTotalPrice(totalPrice);

      // ▶ 여기서 람다를 벗어나면 @Transactional이 커밋 혹은 예외 시 롤백 처리
    });
  }

  @Transactional
  public Purchase process(User user, List<PurchaseProductRequest> purchaseItems) {
    Purchase purchase = createAndSavePurchase(user);
    List<PurchaseProduct> purchaseProducts = createAndProcessPurchaseProducts(purchaseItems,
        purchase);
    BigDecimal totalPrice = calculateTotalPrice(purchaseProducts);

    purchase.setTotalPrice(totalPrice);
    return purchase;
  }

  private Purchase createAndSavePurchase(User user) {
    return purchaseRepository.save(Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.ZERO)
        .status(PurchaseStatus.PENDING)
        .build());
  }

  private List<PurchaseProduct> createAndProcessPurchaseProducts(
      List<PurchaseProductRequest> itemRequests, Purchase purchase) {
    List<PurchaseProduct> purchaseProducts = new ArrayList<>();

    for (PurchaseProductRequest itemRequest : itemRequests) {
      Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow();

      validateStock(itemRequest, product);
      product.reduceStock(itemRequest.getQuantity());

      PurchaseProduct purchaseProduct = PurchaseProduct.builder()
          .product(product)
          .purchase(purchase)
          .quantity(itemRequest.getQuantity())
          .price(product.getPrice())
          .build();

      purchaseProducts.add(purchaseProduct);
    }

    purchaseProductRepository.saveAll(purchaseProducts);
    return purchaseProducts;
  }

  private static void validateStock(PurchaseProductRequest itemRequest, Product product) {
    if (itemRequest.getQuantity() > product.getStock()) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }
  }

  private BigDecimal calculateTotalPrice(List<PurchaseProduct> purchaseProducts) {
    return purchaseProducts.stream()
        .map(purchaseProduct -> purchaseProduct.getPrice()
            .multiply(BigDecimal.valueOf(purchaseProduct.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
