package com.sparta.java_02.domain.purchase.repository;

import com.sparta.java_02.domain.purchase.entity.Purchase;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

  // 언더바를 쓰면 User 안의 Id 필드에 직접 접근 가능. 근데 업데이트 되면서 언더바가 빠져도 된다고 함
  Optional<Purchase> findByIdAndUser_Id(Long id, Long userId);

  // 모든 'PENDING' 상태의 주문을 'COMPLETED'로 한번에 변경
  @Modifying(clearAutomatically = true) // 쿼리 실행 후 영속성 컨텍스트를 자동으로 clear
  @Query("UPDATE Purchase P SET p.status = 'COMPLETED' WHERE p.createdAt < :date AND p.status = 'PENDING'")
  int bulkUpdateStatus(@Param("date") LocalDateTime date);

  @Modifying(clearAutomatically = true)
  @Query("DELETE FROM Purchase p WHERE p.status = 'CANCELED'")
  int deleteCanceledPurchases();
}
