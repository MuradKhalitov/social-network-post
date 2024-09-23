--liquibase formatted sql
--changeset Murad:002-create-comment-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE

CREATE TABLE IF NOT EXISTS post_schema.comment (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    comment_type VARCHAR(50),
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_changed TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    author_id UUID,
    parent_id BIGINT,
    post_id BIGINT,
    comment_text TEXT,
    is_blocked BOOLEAN DEFAULT FALSE,
    is_delete BOOLEAN DEFAULT FALSE,
    like_amount INTEGER DEFAULT 0,
    my_like BOOLEAN DEFAULT FALSE,
    comments_count INTEGER DEFAULT 0,
    image_path VARCHAR(255),
    CONSTRAINT fk_post_comment FOREIGN KEY (post_id) REFERENCES post(id),
    CONSTRAINT fk_parent_comment FOREIGN KEY (parent_id) REFERENCES comment(id)
);

-- Вставка тестовых данных в таблицу comment
INSERT INTO comment (comment_type, author_id, post_id, comment_text)
VALUES ('TEXT', 'd3ae9bc0-a1d7-4bad-9200-c15d37fc5d6a'::uuid, 1, 'This is a comment on the first post'),
       ('TEXT', 'd3ae9bc0-a1d7-4bad-9200-c15d37fc5d6a'::uuid, 2, 'Another comment on the second post');

