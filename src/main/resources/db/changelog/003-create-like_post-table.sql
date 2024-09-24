--liquibase formatted sql
--changeset Murad:003-create-like_post-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE

CREATE TABLE IF NOT EXISTS post_schema.like_post (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    author_id UUID,
    post_id BIGINT,
    CONSTRAINT fk_post_like_post FOREIGN KEY (post_id) REFERENCES post(id)
);

-- Вставка тестовых данных в таблицу like_post
INSERT INTO like_post (author_id, post_id)
VALUES ('5e9573f3-3ee7-40fa-a0d8-d3ce01122848'::uuid, 1),
       ('5e9573f3-3ee7-40fa-a0d8-d3ce01122848'::uuid, 2);
