--liquibase formatted sql
--changeset Murad:004-create-like_comment-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
--CREATE TABLE IF NOT EXISTS post_schema.like_comment (
--    id BIGSERIAL PRIMARY KEY,
--    author_id BIGINT,
--    comment_id BIGINT,
--    time TIMESTAMP
--);

CREATE TABLE IF NOT EXISTS post_schema.like_comment (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    author_id UUID,
    comment_id BIGINT,
    CONSTRAINT fk_comment_like_comment FOREIGN KEY (comment_id) REFERENCES comment(id)
);

-- Вставка тестовых данных в таблицу like_comment
INSERT INTO like_comment (author_id, comment_id)
VALUES ('d3ae9bc0-a1d7-4bad-9200-c15d37fc5d6a'::uuid, 1),
       ('d3ae9bc0-a1d7-4bad-9200-c15d37fc5d6a'::uuid, 2);


