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
VALUES ('d3ae9bc0-a1d7-4bad-9200-c15d37fc5d6a'::uuid, 1),
       ('d3ae9bc0-a1d7-4bad-9200-c15d37fc5d6a'::uuid, 2);
