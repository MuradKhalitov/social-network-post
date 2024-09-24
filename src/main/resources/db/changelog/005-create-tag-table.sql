--liquibase formatted sql
--changeset Murad:005-create-tag-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
CREATE TABLE IF NOT EXISTS post_schema.tag (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Вставка тестовых данных в таблицу tag
INSERT INTO tag (name)
VALUES ('Java'),
       ('Spring');



