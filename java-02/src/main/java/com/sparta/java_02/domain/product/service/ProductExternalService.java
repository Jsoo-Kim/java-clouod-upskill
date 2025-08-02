package com.sparta.java_02.domain.product.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.category.entity.Category;
import com.sparta.java_02.domain.category.repository.CategoryRepository;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import com.sparta.java_02.global.external.client.ExternalShopClient;
import com.sparta.java_02.global.external.dto.ExternalProductResponse;
import com.sparta.java_02.global.external.dto.ExternalProductResponse.ExternalResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductExternalService {

  private final ExternalShopClient externalShopClient;
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;


  /**
   * 단일 페이지 상품 데이터를 API에서 가져와 DB에 저장 (실패 시 ServiceException 던져 트랜잭션 롤백 및 재시도 유도)
   */
  @Transactional
  @Retryable(value = ServiceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
  public void save() {
    try {
      // ① API 호출: 1페이지, 10개 상품 요청
      ExternalProductResponse responses = externalShopClient.getProducts(1, 10);
      log.info("response : {} ", responses.toString());

      // ② 응답 메시지에서 상품 리스트 추출
      List<ExternalResponse> contents = responses.getMessage().getContents();

      // ③ 상품 데이터가 없으면 예외 발생 → 트랜잭션 롤백 & 재시도
      if (contents.isEmpty()) {
        throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
      }

      // ④ 카테고리 조회 (ID=1) → 없으면 예외 발생
      Category category = categoryRepository.findById(1L)
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

      // ⑤ API 응답 데이터 → Product 엔티티 변환
      List<Product> products = new ArrayList<>();
      for (ExternalResponse externalProduct : contents) {
        products.add(Product.builder()
            .name(externalProduct.getName())
            .description(externalProduct.getDescription())
            .stock(externalProduct.getStock())
            .price(externalProduct.getPrice())
            .category(category)
            .build());
      }

      // ⑥ 변환된 엔티티를 한 번에 저장
      productRepository.saveAll(products);

    } catch (Exception error) {
      // ⑦ 기타 예외 발생 시 ServiceException으로 감싸서 던짐
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
    }
  }

  /**
   * 전체 페이지를 순회하며 외부 상품 데이터를 가져와 DB에 저장 (실패 시 ServiceException 던져 전체 트랜잭션 롤백 및 재시도 유도)
   */
  @Transactional
  @Retryable(value = ServiceException.class, maxAttempts = 10, backoff = @Backoff(delay = 1000))
  public void saveAllExternalProducts() {
    int page = 0;               // 시작 페이지
    int pageSize = 10;          // 한 페이지당 요청 크기
    boolean lastPage = false;   // 마지막 페이지 여부 플래그

    while (!lastPage) {
      // ① 해당 페이지 API 호출
      ExternalProductResponse responses = externalShopClient.getProducts(page, pageSize);
      log.info("Response for page {}: {}", page, responses);

      // ② 응답 또는 메시지 객체가 null이면 예외 발생 → 롤백 & 재시도
      if (Objects.isNull(responses) || Objects.isNull(responses.getMessage())) {
        throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
      }

      // ③ 메시지에서 상품 리스트 추출
      List<ExternalProductResponse.ExternalResponse> contents = responses.getMessage()
          .getContents();

      // ④ 빈 리스트거나 null이면 루프 종료 (더 이상 데이터 없음)
      if (Objects.isNull(contents) || contents.isEmpty()) {
        break;
      }

      // ⑤ 카테고리 조회 (ID=1) → 없으면 예외 발생
      Category category = categoryRepository.findById(1L)
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

      // ⑥ API 응답 데이터 → Product 엔티티 변환
      List<Product> products = new ArrayList<>();
      for (ExternalProductResponse.ExternalResponse externalProduct : contents) {
        Product product = Product.builder()
            .name(externalProduct.getName())
            .description(externalProduct.getDescription())
            .stock(externalProduct.getStock())
            .price(externalProduct.getPrice())
            .category(category)
            .build();
        products.add(product);
      }

      // ⑦ 변환된 엔티티를 한 번에 저장
      productRepository.saveAll(products);

      // ⑧ 페이징 정보로 마지막 페이지 판단
      ExternalProductResponse.ExternalPageable pageable = responses.getMessage().getPageable();
      if (Objects.nonNull(pageable)) {
        lastPage = pageable.isLast();
      } else {
        lastPage = contents.size() < pageSize;
      }

      page++;  // ⑨ 다음 페이지로 이동
    }
  }

}