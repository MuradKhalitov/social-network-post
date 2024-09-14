--liquibase formatted sql
--changeset Murad:000-create-users-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
CREATE TABLE IF NOT EXISTS post_schema.users (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Вставка тестовых данных в таблицу users
INSERT INTO users (id, email, password, role)
VALUES ('60b1f478-ec5a-4cfa-a022-ee9713228a86'::uuid, 'tagir@gmail.com', '$2a$10$sitWvAIfWKh4mTbitcr1hOK1AdNVv1h.i8c8e/nvkTWHMl6mL04ca', 'ROLE_USER'),
       ('df68c55b-5909-4096-bec8-b69e174123dd'::uuid, 'admin@gmail.com', '$2a$10$sitWvAIfWKh4mTbitcr1hOK1AdNVv1h.i8c8e/nvkTWHMl6mL04ca', 'ROLE_ADMIN');