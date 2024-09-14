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
    CONSTRAINT fk_author_like_comment FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_comment_like_comment FOREIGN KEY (comment_id) REFERENCES comment(id)
);

-- Вставка тестовых данных в таблицу like_comment
INSERT INTO like_comment (author_id, comment_id)
VALUES ('60b1f478-ec5a-4cfa-a022-ee9713228a86'::uuid, 1),
       ('df68c55b-5909-4096-bec8-b69e174123dd'::uuid, 2);


