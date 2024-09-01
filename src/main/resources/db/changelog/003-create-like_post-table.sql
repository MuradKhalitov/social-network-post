--liquibase formatted sql
--changeset Murad:003-create-like_post-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
CREATE TABLE IF NOT EXISTS post_schema.like_post (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT,
    post_id BIGINT,
    time TIMESTAMP
);