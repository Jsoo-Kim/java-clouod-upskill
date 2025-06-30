package com.sparta.java_02.domain.user.repository;

import static com.sparta.java_02.domain.purchase.entity.QPurchase.purchase;
import static com.sparta.java_02.domain.user.entity.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.java_02.domain.user.dto.QUserPurchaseResponse;
import com.sparta.java_02.domain.user.dto.UserPurchaseResponse;
import com.sparta.java_02.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

  private final JPAQueryFactory queryFactory;

  public List<User> findUser(String name, String email) {
    return queryFactory.select(user)
        .from(user)
        .join(purchase).on(user.eq(purchase.user))
        .where(
            nameContains(name),
            emailContains(email)
        )
        .fetch();
  }

  public Page<User> findPagedUser(String name, String email, Pageable pageable) {
    List<User> users = queryFactory
        .select(user)
        .from(user)
        .join(purchase).on(user.eq(purchase.user))
        .where(
            nameContains(name),
            emailContains(email)
        )
        .offset(pageable.getOffset())  // 시작점
        .limit(pageable.getPageSize())  // 시작점에서 몇 개 가져올지
        .fetch();

    Long totalCount = queryFactory
        .select(user.count())
        .from(user)
        .join(purchase).on(user.eq(purchase.user))
        .where(
            nameContains(name),
            emailContains(email)
        )
        .fetchOne();

    return new PageImpl<>(users, pageable, totalCount);
  }

  public Page<UserPurchaseResponse> findUserPurchaseResponse(String name, String email,
      Pageable pageable) {

    List<UserPurchaseResponse> users = queryFactory
        .select(new QUserPurchaseResponse(
            user.id,
            user.name,
            user.email,
            purchase.id,
            purchase.totalPrice
        ))
        .from(user)
        .join(purchase).on(user.eq(purchase.user))
        .where(
            nameContains(name),
            emailContains(email)
        )
        .offset(pageable.getOffset())  // 시작점
        .limit(pageable.getPageSize())  // 시작점에서 몇 개 가져올지
        .fetch();

    Long totalCount = queryFactory
        .select(user.count())
        .from(user)
        .join(purchase).on(user.eq(purchase.user))
        .where(
            nameContains(name),
            emailContains(email)
        )
        .fetchOne();

    return new PageImpl<>(users, pageable, totalCount);
  }

  private BooleanExpression nameContains(String name) {
    return StringUtils.hasText(name) ? user.name.contains(name) : null;
  }

  private BooleanExpression emailContains(String email) {
    return StringUtils.hasText(email) ? user.email.contains(email) : null;
  }
}
