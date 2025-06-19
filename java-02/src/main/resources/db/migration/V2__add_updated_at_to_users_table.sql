-- V2: users 테이블에 updated_at 추가
ALTER TABLE users
    ADD COLUMN updated_at DATETIME
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP;
