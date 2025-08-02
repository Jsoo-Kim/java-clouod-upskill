package com.sparta.java_02.domain.product.repository;

import com.sparta.java_02.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//  @Modifying(clearAutomatically = true) // 쿼리 실행 후 영속성 컨텍스트 자동 클리어
//  @Query("UPDATE Product p SET p.price = p.price * 0.9 WHERE p.category = :categoryId")
//  int applyDiscountByCategory(@Param("categoryId") Long categoryId);

  List<Product> findAllByStockGreaterThan(Integer stock);

}
