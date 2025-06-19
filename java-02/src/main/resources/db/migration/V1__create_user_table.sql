CREATE TABLE user (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,

   name VARCHAR(50) NOT NULL, -- 이미 users 테이블 안에 있으니 username보다 name이 깔끔
   email VARCHAR(255) NOT NULL UNIQUE,
   password_hash VARCHAR(255) NOT NULL,

   created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);