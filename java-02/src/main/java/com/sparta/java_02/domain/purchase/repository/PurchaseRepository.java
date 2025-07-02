package com.sparta.java_02.domain.purchase.repository;

import com.sparta.java_02.domain.purchase.entity.Purchase;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

  Optional<Purchase> findByIdAndUser_Id(Long id,
      Long userId);  // 언더바를 쓰면 User 안의 Id 필드에 직접 접근 가능. 근데 업데이트 되면서 언더바가 빠져도 된다고 함

}
