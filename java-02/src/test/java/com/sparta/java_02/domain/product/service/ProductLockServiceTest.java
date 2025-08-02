package com.sparta.java_02.domain.product.service;

import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class ProductLockServiceTest {

  private static final Logger log = LoggerFactory.getLogger(ProductLockServiceTest.class);

  @Autowired
  private ProductLockService productLockService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Test
  public void updateStockWithPessimisticLock() throws InterruptedException {
    // ① given: 테스트에 사용할 상품 ID와 동시에 실행할 스레드 수 설정
    Long productId = 1L;
    int threadCount = 2;

    // ② 초기 상태 조회: 테스트 전 재고 값 저장
    Product firstProduct = productRepository.findById(productId).orElseThrow();

    // ③ ExecutorService 생성: 고정 크기 스레드 풀 준비
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    // ④ CountDownLatch 생성: 모든 스레드가 끝날 때까지 대기하기 위해 사용
    CountDownLatch latch = new CountDownLatch(threadCount);

    //when: 여러 스레드로 동시에 update 호출
    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          // ⑤ 각 스레드에서 Pessimistic Lock을 사용해 재고 1 감소
          productLockService.updateStockWithPessimisticLock(productId, 1);
        } finally {
          // ⑥ 작업 완료 시 latch 카운트 감소
          latch.countDown();
        }
      });
    }

    // ⑦ 모든 스레드가 작업을 마칠 때까지 대기
    latch.await();

    //then: 최종 재고 값 검증 (처음 재고에서 threadCount만큼 빠져야 함)
    Product product = productRepository.findById(productId).orElseThrow();
    Assertions.assertThat(product.getStock()).isEqualTo(firstProduct.getStock() - 2);
  }

  @Test
  public void updateStockWithOptimisticLock() throws InterruptedException {
    // ① given: 상품 ID와 스레드 수 설정
    Long productId = 1L;
    int threadCount = 2;

    // ② 테스트 시작 전 초기 재고 별도 저장
    Product initialProduct = productRepository.findById(productId).orElseThrow();
    int initialStock = initialProduct.getStock();

    // ③ ExecutorService와 CountDownLatch 초기화
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    // when: 여러 스레드가 동시에 Optimistic Lock 기반 update 시도
    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          // ④ Optimistic Lock 적용 서비스 호출
          // Product의 name 대신 id를 넘겨주는 것이 더 명확
          productLockService.updateStockWithOptimisticLock(productId, 1);
        } catch (Exception e) {
          // ⑤ 버전 충돌 발생 시 로그만 남기고 해당 스레드는 실패
          // OptimisticLockException 예외가 발생하여 하나의 스레드는 실패할 것으로 예상
          log.info("예상된 충돌 발생: " + e.getMessage());
        } finally {
          // ⑥ 완료 처리: latch 카운트 감소
          latch.countDown();
        }
      });
    }

    // ⑦ 모든 스레드 작업 완료 대기
    latch.await();

    // then: 새로운 트랜잭션에서 최종 재고 조회
    Integer finalStock = transactionTemplate.execute(status -> {
      Product productAfterUpdate = productRepository.findById(productId).orElseThrow();
      return productAfterUpdate.getStock();
    });

    // ⑧ 하나의 트랜잭션만 성공했으므로 재고가 1만큼만 감소했는지 확인
    Assertions.assertThat(finalStock).isEqualTo(initialStock - 1);
  }
}