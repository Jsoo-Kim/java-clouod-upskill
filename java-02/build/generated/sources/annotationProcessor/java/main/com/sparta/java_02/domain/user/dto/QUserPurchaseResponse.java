package com.sparta.java_02.domain.user.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.sparta.java_02.domain.user.dto.QUserPurchaseResponse is a Querydsl Projection type for UserPurchaseResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QUserPurchaseResponse extends ConstructorExpression<UserPurchaseResponse> {

    private static final long serialVersionUID = -620484502L;

    public QUserPurchaseResponse(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<Long> purchaseId, com.querydsl.core.types.Expression<? extends java.math.BigDecimal> purchaseTotalPrice) {
        super(UserPurchaseResponse.class, new Class<?>[]{long.class, String.class, String.class, long.class, java.math.BigDecimal.class}, id, name, email, purchaseId, purchaseTotalPrice);
    }

}

