--liquibase formatted sql
--changeset Murad:002-create-comment-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE

--CREATE TABLE IF NOT EXISTS post_schema.comment (
--    id BIGSERIAL PRIMARY KEY,
--    comment_type VARCHAR(255),
--    time TIMESTAMP,
--    time_changed TIMESTAMP,
--    author_id BIGINT,
--    parent_id BIGINT,
--    comment_text VARCHAR(255),
--    post_id BIGINT,
--    is_blocked BOOLEAN DEFAULT FALSE,
--    is_delete BOOLEAN DEFAULT FALSE,
--    like_amount INT DEFAULT 0,
--    my_like BOOLEAN DEFAULT FALSE,
--    comments_count INT DEFAULT 0,
--    image_path VARCHAR(255)
--);

CREATE TABLE IF NOT EXISTS post_schema.comment (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    comment_type VARCHAR(50),
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_changed TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id BIGINT,
    parent_id BIGINT,
    post_id BIGINT,
    comment_text TEXT,
    is_blocked BOOLEAN DEFAULT FALSE,
    is_delete BOOLEAN DEFAULT FALSE,
    like_amount INTEGER DEFAULT 0,
    my_like BOOLEAN DEFAULT FALSE,
    comments_count INTEGER DEFAULT 0,
    image_path VARCHAR(255),
    CONSTRAINT fk_author_comment FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_post_comment FOREIGN KEY (post_id) REFERENCES post(id),
    CONSTRAINT fk_parent_comment FOREIGN KEY (parent_id) REFERENCES comment(id)
);

-- Вставка тестовых данных в таблицу comment
INSERT INTO comment (comment_type, author_id, post_id, comment_text)
VALUES ('TEXT', 1, 1, 'This is a comment on the first post'),
       ('TEXT', 2, 1, 'Another comment on the first post');

