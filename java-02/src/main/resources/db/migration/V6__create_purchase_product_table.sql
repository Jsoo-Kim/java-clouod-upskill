-- V6: PurchaseProduct 매핑용 테이블 생성
CREATE TABLE purchase_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase BIGINT   NOT NULL,
    product  BIGINT   NOT NULL,
    created_at DATETIME
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pp_purchase
        FOREIGN KEY (purchase)
        REFERENCES purchase(id),
    CONSTRAINT fk_pp_product
        FOREIGN KEY (product)
        REFERENCES product(id)
);
