--liquibase formatted sql
--changeset Murad:004-create-like_comment-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
CREATE TABLE IF NOT EXISTS post_schema.like_comment (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT,
    comment_id BIGINT,
    time TIMESTAMP
);

