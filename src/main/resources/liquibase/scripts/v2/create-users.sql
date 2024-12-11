-- liquibase formatted sql

-- changeset danil:v2-1-1
CREATE TABLE telegram_users(
    id bigserial primary key,
    telegram_id BIGINT NOT NULL,
    warning_counter INT default 0
)
