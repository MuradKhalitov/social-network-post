--liquibase formatted sql
--changeset Murad:001-create-post-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE

CREATE TABLE IF NOT EXISTS post_schema.post (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(255),
    post_text TEXT,
    author_id UUID,
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_changed TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    type VARCHAR(50),
    is_blocked BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    comments_count INTEGER DEFAULT 0,
    like_amount INTEGER DEFAULT 0,
    my_like BOOLEAN DEFAULT FALSE,
    image_path VARCHAR(255),
    publish_date TIMESTAMP
);

-- Вставка тестовых данных в таблицу post
INSERT INTO post (title, post_text, author_id, type, publish_date)
VALUES ('First Post', 'This is the content of the first post', 'e61e54d7-3f0e-4bf2-b7d3-24663f35e506'::uuid, 'POSTED', CURRENT_TIMESTAMP),
       ('Second Post', 'This is the content of the second post', 'e61e54d7-3f0e-4bf2-b7d3-24663f35e506'::uuid, 'POSTED', CURRENT_TIMESTAMP);
