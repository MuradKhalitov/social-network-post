--liquibase formatted sql
--changeset Murad:004-create-like_comment-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE

CREATE TABLE IF NOT EXISTS post_schema.like_comment (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    author_id UUID,
    comment_id BIGINT,
    CONSTRAINT fk_comment_like_comment FOREIGN KEY (comment_id) REFERENCES comment(id)
);

-- Вставка тестовых данных в таблицу like_comment
INSERT INTO like_comment (author_id, comment_id)
VALUES ('e61e54d7-3f0e-4bf2-b7d3-24663f35e506'::uuid, 1),
       ('e61e54d7-3f0e-4bf2-b7d3-24663f35e506'::uuid, 2);


