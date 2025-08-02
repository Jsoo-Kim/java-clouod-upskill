package com.sparta.java_02.domain.product.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductTransactionService {

  private final PlatformTransactionManager transactionManager;
  private final ProductRepository productRepository;

  // 프로그래밍 방식 트랜잭션 적용 예제
  public void updateProductStock(Long productId, int quantity) {
    // 새 트랜잭션 시작
    // DefaultTransactionDefinition: 기본 트랜잭션 속성 제공, 필요 시 격리 수준(isolation)이나 전파(propagation) 옵션 설정 가능
    TransactionStatus status = transactionManager.getTransaction(
        new DefaultTransactionDefinition());

    try {
      Product product = productRepository.findById(productId)
          .orElseThrow(() -> new RuntimeException("Product not found"));

      if (product.getStock() < quantity) {
        throw new IllegalArgumentException("Insufficient stock");
      }

      product.reduceStock(quantity);
      productRepository.save(product);

      log.info("isTransaction : {}", TransactionSynchronizationManager.isActualTransactionActive());

      // 모든 작업이 정상적으로 완료되었다면 데이터베이스에 변경 사항 반영
      transactionManager.commit(status);

    } catch (Exception ex) {
      // 비즈니스 로직 중 예외가 발생하면 catch 블록에서 transactionManager.rollback(status)를 호출하여 트랜잭션을 롤백
      transactionManager.rollback(status);
      throw ex;
    }
  }

  // 선언적 트랜잭션 적용 예제 1
  @Transactional(readOnly = true)
  public Product getProduct(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
  }

  // 선언적 트랜잭션 적용 예제 2
  @Transactional
  public void updateProductStockTransactional(Long productId, int quantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    if (product.getStock() < quantity) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }

    product.reduceStock(quantity);

    log.info("isTransaction : {}", TransactionSynchronizationManager.isActualTransactionActive());
  }

}
