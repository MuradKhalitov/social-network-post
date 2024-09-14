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
    author_id UUID,
    post_id BIGINT,
    CONSTRAINT fk_author_like_post FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_post_like_post FOREIGN KEY (post_id) REFERENCES post(id)
);

-- Вставка тестовых данных в таблицу like_post
INSERT INTO like_post (author_id, post_id)
VALUES ('60b1f478-ec5a-4cfa-a022-ee9713228a86'::uuid, 1),
       ('df68c55b-5909-4096-bec8-b69e174123dd'::uuid, 1);
