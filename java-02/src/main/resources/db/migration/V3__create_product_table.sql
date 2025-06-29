-- V4: Product 엔티티용 테이블 생성
CREATE TABLE product (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     category_id BIGINT
         NOT NULL,
     name VARCHAR(255)
         NOT NULL,
     description TEXT,
     price DECIMAL(10, 2)
         NOT NULL,
     stack INT
         NOT NULL,
     created_at DATETIME
         DEFAULT CURRENT_TIMESTAMP,
     updated_at DATETIME
         NOT NULL
         DEFAULT CURRENT_TIMESTAMP
         ON UPDATE CURRENT_TIMESTAMP
);
