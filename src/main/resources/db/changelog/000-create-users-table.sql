--liquibase formatted sql
--changeset Murad:000-create-users-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
CREATE TABLE IF NOT EXISTS post_schema.users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Вставка тестовых данных в таблицу users
INSERT INTO users (email, password, role)
VALUES ('tagir@gmail.com', '$2a$10$sitWvAIfWKh4mTbitcr1hOK1AdNVv1h.i8c8e/nvkTWHMl6mL04ca', 'ROLE_USER'),
       ('admin@gmail.com', '$2a$10$sitWvAIfWKh4mTbitcr1hOK1AdNVv1h.i8c8e/nvkTWHMl6mL04ca', 'ROLE_ADMIN');