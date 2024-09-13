--liquibase formatted sql
--changeset Murad:003-create-like_post-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
--CREATE TABLE IF NOT EXISTS post_schema.like_post (
--    id BIGSERIAL PRIMARY KEY,
--    author_id BIGINT,
--    post_id BIGINT,
--    time TIMESTAMP
--);

CREATE TABLE IF NOT EXISTS post_schema.like_post (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    author_id BIGINT,
    post_id BIGINT,
    CONSTRAINT fk_author_like_post FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_post_like_post FOREIGN KEY (post_id) REFERENCES post(id)
);

-- Вставка тестовых данных в таблицу like_post
INSERT INTO like_post (author_id, post_id)
VALUES (1, 1),
       (2, 1);
