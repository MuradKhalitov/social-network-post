--liquibase formatted sql
--changeset Murad:003-create-like_post-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE

CREATE TABLE IF NOT EXISTS post_schema.like_post (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    author_id UUID,
    post_id BIGINT,
    reaction_type VARCHAR(20),
    created_at TIMESTAMP,
    CONSTRAINT fk_post_like_post FOREIGN KEY (post_id) REFERENCES post_schema.post(id) ON DELETE CASCADE
);
