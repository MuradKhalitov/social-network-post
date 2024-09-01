--liquibase formatted sql
--changeset Murad:002-create-comment-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE

CREATE TABLE IF NOT EXISTS post_schema.comment (
    id BIGSERIAL PRIMARY KEY,
    comment_type VARCHAR(255),
    time TIMESTAMP,
    time_changed TIMESTAMP,
    author_id BIGINT,
    parent_id BIGINT,
    comment_text VARCHAR(255),
    post_id BIGINT,
    is_blocked BOOLEAN DEFAULT FALSE,
    is_delete BOOLEAN DEFAULT FALSE,
    like_amount INT DEFAULT 0,
    my_like BOOLEAN DEFAULT FALSE,
    comments_count INT DEFAULT 0,
    image_path VARCHAR(255)
);
