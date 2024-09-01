--liquibase formatted sql
--changeset Murad:001-create-post-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE

CREATE TABLE IF NOT EXISTS post_schema.post(
    id BIGSERIAL PRIMARY KEY,
    time TIMESTAMP NOT NULL,
    time_changed TIMESTAMP,
    author_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(255),
    post_text VARCHAR(255),
    is_blocked BOOLEAN DEFAULT FALSE,
    is_delete BOOLEAN DEFAULT FALSE,
    comments_count INT DEFAULT 0,
    tags VARCHAR(255),
    like_amount INT DEFAULT 0,
    my_like BOOLEAN DEFAULT FALSE,
    image_path VARCHAR(255),
    publish_date TIMESTAMP
);
