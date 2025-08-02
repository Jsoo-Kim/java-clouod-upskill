package com.sparta.java_02.domain.product.repository;

import com.sparta.java_02.domain.product.entity.Product;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//  @Modifying(clearAutomatically = true) // 쿼리 실행 후 영속성 컨텍스트 자동 클리어
//  @Query("UPDATE Product p SET p.price = p.price * 0.9 WHERE p.category = :categoryId")
//  int applyDiscountByCategory(@Param("categoryId") Long categoryId);

  List<Product> findAllByStockGreaterThan(Integer stock);


  // PESSIMISTIC_WRITE: 읽기와 쓰기 모두 차단하여 다른 트랜잭션이 해당 데이터에 접근하지 못하도록 막음. SELECT ... FOR UPDATE와 유사
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Product p WHERE p.id = :id")
  Optional<Product> findByIdForUpdate(@Param("id") Long id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Product> findFirstByName(String name);

  @Lock(LockModeType.OPTIMISTIC)
  Optional<Product> findByIdOrderById(Long productId);

}
