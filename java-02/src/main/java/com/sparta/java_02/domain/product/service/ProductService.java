package com.sparta.java_02.domain.product.service;

import com.sparta.java_02.common.exception.CustomCheckedException;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.category.entity.Category;
import com.sparta.java_02.domain.category.repository.CategoryRepository;
import com.sparta.java_02.domain.product.dto.ProductRequest;
import com.sparta.java_02.domain.product.dto.ProductResponse;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Transactional
  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream()
        .map(product -> ProductResponse.builder()
            .id(product.getId())
            .categoryId(product.getCategory().getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .createdAt(product.getCreatedAt())
            .build())
        .collect(Collectors.toList());
  }

  @Transactional
  public ProductResponse getById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    return ProductResponse.builder()
        .id(product.getId())
        .categoryId(product.getCategory().getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .stock(product.getStock())
        .createdAt(product.getCreatedAt())
        .build();
  }

  @Transactional
  public void save(ProductRequest request) {
    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    productRepository.save(Product.builder()
        .category(category)
        .name(request.getName())
        .description(request.getDescription())
        .price(request.getPrice())
        .stock(request.getStock())
        .build());
  }

  /*
   * 상품 재고를 지정된 수량으로 업데이트합니다.
   *
   * Spring의 기본 규칙: RuntimeException(RuntimeException 계열)과 Error 발생 시 롤백
   * rollbackFor 추가 설정: CustomCheckedException(체크 예외) 발생 시에도 롤백
   */
  @Transactional(rollbackFor = CustomCheckedException.class)
  public void updateProductQuantity(Long productId, Integer quantity)
      throws CustomCheckedException {
    // ① 상품 조회 (없으면 ServiceException → RuntimeException → 기본 롤백 대상)
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    // ② 재고 변경 시도 로그
    log.info("상품 재고를 {}에서 {}로 변경 시도.", product.getPrice(), quantity);

    // ③ 재고 업데이트 후 저장 (DB 반영)
    product.setStock(quantity);
    productRepository.save(product); // 변경 사항을 우선 DB에 반영

    // ④ 체크 예외: 수량이 음수면 CustomCheckedException 발생 → rollbackFor 지정 덕분에 롤백 (체크 예외)
    if (quantity < 0) {
      throw new CustomCheckedException("재고는 음수가 될 수 없습니다.");
    }
  }

  /*
   * 상품 재고를 차감합니다.
   *
   * Spring의 기본 규칙: RuntimeException과 Error 발생 시 롤백
   * noRollbackFor 설정: IllegalArgumentException(언체크 예외) 발생 시 롤백하지 않고 커밋
   */
  @Transactional(noRollbackFor = IllegalArgumentException.class)
  public void reduceProductStockNoRollback(Long productId, Integer quantity) {
    // ① 상품 조회 (없으면 ServiceException → RuntimeException → 기본 롤백 대상)
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    // ② 예시: 로그 저장 등 다른 DB 작업
    //    logRepository.save(new Log("재고 차감 시도..."));

    // ③ 재고 부족 시 IllegalArgumentException 발생 (언체크 예외)
    //    → noRollbackFor 지정으로 인해 예외가 던져져도 트랜잭션은 커밋됨
    if (product.getStock() < quantity) {
      throw new IllegalArgumentException("재고가 부족합니다.");
    }

    // ④ 재고 차감 후 저장 (예외가 발생한 경우 이 줄은 아예 실행되지 않음)
    product.reduceStock(quantity);
    productRepository.save(product);
  }


}
