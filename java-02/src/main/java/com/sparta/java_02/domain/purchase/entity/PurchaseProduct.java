package com.sparta.java_02.domain.purchase.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.java_02.domain.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 구매-상품 매핑 엔티티
 * <p>
 * Purchase와 Product 간 다대다(N:N) 관계를 연결하기 위한 순수 조인 테이블입니다. 매핑 관계에서 Purchase 기준으로 데이터를 조회하는 빈도가 높습니다.
 * 예) GET /api/purchases/{purchaseId}/products
 * <p>
 * N:N 중간 테이블은 별도의 추가 정보를 담지 않고 순수한 관계만 표현합니다. 관계 변경 시 DELETE 후 INSERT 방식으로 레코드를 교체하여 무결성과 이력 관리가
 * 간단해지므로, 별도의 수정 시간(updatedAt) 컬럼은 정의하지 않습니다.
 *
 * <strong>생성자·빌더 미사용 이유</strong>
 * <ul>
 *   <li>단순 관계 표현: Purchase ↔ Product 연결만 수행, 추가 필드가 없어 복잡한 생성 로직 불필요</li>
 *   <li>JPA 연관관계 편의 메서드 사용: 부모 엔티티 메서드 내에서 직접 객체 생성 및 관리</li>
 *   <li>불변성 보장: 생성 후 변경 대신 DELETE+INSERT로 이력 관리, 객체 상태 변경 없음</li>
 *   <li>코드 가독성: 순수 조인 테이블에 과도한 어노테이션·빌더는 오히려 불필요한 복잡도 유발</li>
 * </ul>
 */
@Table
@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseProduct {
  // 매핑 관계에서 purchase/product 가 product/purchase 보다 조회될 일 이 많을 것 같다! 그러므로 purchase가 N:N 관계에서의 메인

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @JsonBackReference
  @JoinColumn(name = "purchase_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  Purchase purchase;

  @JsonBackReference
  @JoinColumn(name = "product_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  Product product;

  @Column(nullable = false)
  Integer quantity;

  @Column(nullable = false)
  BigDecimal price;
  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  LocalDateTime createdAt;

  @CreationTimestamp
  @Column(nullable = false)
  LocalDateTime updatedAt;

  @Builder
  public PurchaseProduct(Purchase purchase, Product product, Integer quantity, BigDecimal price) {
    this.purchase = purchase;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
  }

}
