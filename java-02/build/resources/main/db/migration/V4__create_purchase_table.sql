-- V5: Purchase 엔티티용 테이블 생성
CREATE TABLE purchase (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      user_id BIGINT
          NOT NULL,
      total_price DECIMAL(19,2)
          NOT NULL,
      status VARCHAR(20)
          NOT NULL,
      created_at DATETIME
          NOT NULL
          DEFAULT CURRENT_TIMESTAMP,
      updated_at DATETIME
          NOT NULL
          DEFAULT CURRENT_TIMESTAMP
          ON UPDATE CURRENT_TIMESTAMP,
      -- CONSTRAINT fk_purchase_user
      --     FOREIGN KEY (user_id)
      --     REFERENCES user(id)
);
