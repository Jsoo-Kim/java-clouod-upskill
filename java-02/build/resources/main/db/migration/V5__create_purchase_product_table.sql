-- V6: PurchaseProduct 매핑용 테이블 생성
CREATE TABLE purchase_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_id BIGINT
        NOT NULL
        COMMENT '어떤 주문에 속하는지',
    product_id  BIGINT
        NOT NULL
        COMMENT '어떤 상품인지',
    quantity INT
        NOT NULL,
    price DECIMAL(10, 2)
        NOT NULL
        COMMENT '주문 시점의 상품 가격',
    created_at DATETIME(6)
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),
    -- CONSTRAINT fk_pp_purchase
    --     FOREIGN KEY (purchase)
    --     REFERENCES purchase(id),
    -- CONSTRAINT fk_pp_product
    --     FOREIGN KEY (product)
    --     REFERENCES product(id)
);
