package com.sparta.java_02.domain.product.repository;

import com.sparta.java_02.domain.product.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Modifying(clearAutomatically = true) // 쿼리 실행 후 영속성 컨텍스트 자동 클리어
  @Query("UPDATE Product p SET p.price = p.price * 0.9 WHERE p.categoryId = :categoryId")
  int applyDiscountByCategory(@Param("categoryId") Long categoryId);

}
