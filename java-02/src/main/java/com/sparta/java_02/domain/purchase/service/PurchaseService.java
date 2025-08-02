package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.common.enums.TaskType;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelRequest;
import com.sparta.java_02.domain.purchase.dto.PurchaseCancelResponse;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequest;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.task.entity.TaskQueue;
import com.sparta.java_02.domain.task.service.TaskQueueService;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final TaskQueueService taskQueueService;
  private final PurchaseProcessService purchaseProcessService;
  private final PurchaseCancelService purchaseCancelService;

  private final UserRepository userRepository;

  @Transactional
  public void purchaseRequest(PurchaseRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    TaskQueue taskQueue = taskQueueService.requestQueue(TaskType.PURCHASE);
    purchaseProcessService.purchaseProcess(taskQueue.getId(), request, user);
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

  @Transactional
  public User getUser(Long userId, ServiceExceptionCode code) {
    return userRepository.findById(userId) // <- 기능
        .orElseThrow(() -> new ServiceException(code)); // 결과
  }

}
