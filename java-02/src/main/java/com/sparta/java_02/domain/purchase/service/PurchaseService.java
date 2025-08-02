package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.common.enums.TaskType;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelRequest;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelResponse;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequest;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.entity.PurchaseProduct;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.task.entity.TaskQueue;
import com.sparta.java_02.domain.task.repository.TaskQueueRepository;
import com.sparta.java_02.domain.task.service.TaskQueueService;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final TaskQueueService taskQueueService;
  private final PurchaseProcessService purchaseProcessService;
  private final PurchaseCancelService purchaseCancelService;

  private final UserRepository userRepository;
  private final PurchaseRepository purchaseRepository;
  private final TaskQueueRepository taskQueueRepository;

  @Transactional
  public void purchaseRequest(PurchaseRequest request) {
    TaskQueue taskQueue = taskQueueService.requestQueue(TaskType.PURCHASE);
    purchaseProcess(taskQueue.getId(), request);
  }

  @Async  // ① 이 메서드를 호출한 스레드와 별도로, Spring이 관리하는 스레드 풀에서 비동기로 실행
  @Transactional  // ② 이 메서드 전체를 하나의 트랜잭션으로 묶어서 커밋·롤백을 자동으로 처리
  public void purchaseProcess(Long taskQueueId, PurchaseRequest request) {
    // ③ TaskQueue를 조회하고 FOR UPDATE 잠금을 걺
    // 내부에서 updateStatus(PROCESSING) → 작업 람다 실행 → updateStatus(COMPLETED) 흐름이 관리됨
    taskQueueService.processQueueById(taskQueueId, (taskQueue) -> {

      // ④ 요청에 담긴 userId로 User 엔티티 조회 → 없으면 NOT_FOUND_USER 예외 던짐
      User user = userRepository.findById(request.getUserId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

      // ⑤ Purchase 엔티티 생성·저장, 생성된 purchase 객체를 반환 받음
      Purchase purchase = purchaseProcessService.createAndSavePurchase(user);

      // ⑥ TaskQueue에 생성된 Purchase 이벤트 ID를 설정해서 이후 로깅이나 후처리에서 참조할 수 있게 함
      taskQueue.setEventId(purchase.getId());

      // ⑦ 각 주문 상품(PurchaseProduct)을 생성하고, 재고 차감 등 처리 로직을 수행
      List<PurchaseProduct> purchaseProducts = purchaseProcessService.createAndProcessPurchaseProducts(
          request.getProducts(),
          purchase);

      // ⑧ 처리된 PurchaseProduct 목록을 바탕으로 총 결제 금액을 계산해 Purchase 엔티티에 설정
      BigDecimal totalPrice = purchaseProcessService.calculateTotalPrice(purchaseProducts);
      purchase.setTotalPrice(totalPrice);

      // ▶ 트랜잭션 경계는 processQueueById가 관리하므로 여기서 커밋됨
    });
  }


  public Purchase purchase(PurchaseRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    return purchaseProcessService.process(user, request.getProducts());
  }

  @Transactional
  public PurchaseCancelResponse cancel(PurchaseCancelRequest request) {
    User user = getUser(request.getUserId(), ServiceExceptionCode.NOT_FOUND_USER);
    // user 검증은 Auth 에서 수행 했다고 가정
    return purchaseCancelService.cancelPurchase(request.getPurchaseId(), request.getUserId());
  }

  public User getUser(Long userId, ServiceExceptionCode code) {
    return userRepository.findById(userId) // <- 기능
        .orElseThrow(() -> new ServiceException(code)); // 결과
  }

}
