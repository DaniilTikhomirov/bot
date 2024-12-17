-- liquibase formatted sql

-- changeset danil:v2-1-1
CREATE TABLE telegram_users(
    id bigserial primary key,
    telegram_id BIGINT NOT NULL UNIQUE,
    warning_counter INT default 0
);

-- changeset danil:v2-1-2
ALTER TABLE telegram_users ADD COLUMN volunteers_telegram_id BIGINT;
ALTER TABLE telegram_users ADD FOREIGN KEY (volunteers_telegram_id) REFERENCES volunteers(telegram_id);

