--liquibase formatted sql
--changeset Murad:001-create-post-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE



--CREATE TABLE IF NOT EXISTS post_schema.post(
--    id BIGSERIAL PRIMARY KEY,
--    time TIMESTAMP NOT NULL,
--    time_changed TIMESTAMP,
--    author_id BIGINT NOT NULL,
--    title VARCHAR(255) NOT NULL,
--    type VARCHAR(255),
--    post_text VARCHAR(255),
--    is_blocked BOOLEAN DEFAULT FALSE,
--    is_deleted BOOLEAN DEFAULT FALSE,
--    comments_count INT DEFAULT 0,
--    tags VARCHAR(255),
--    like_amount INT DEFAULT 0,
--    my_like BOOLEAN DEFAULT FALSE,
--    image_path VARCHAR(255),
--    publish_date TIMESTAMP
--);

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
    publish_date TIMESTAMP,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users(id)
);

-- Вставка тестовых данных в таблицу post
INSERT INTO post (title, post_text, author_id, type, publish_date)
VALUES ('First Post', 'This is the content of the first post', '60b1f478-ec5a-4cfa-a022-ee9713228a86'::uuid, 'PUBLIC', CURRENT_TIMESTAMP),
       ('Second Post', 'This is the content of the second post', 'df68c55b-5909-4096-bec8-b69e174123dd'::uuid, 'PUBLIC', CURRENT_TIMESTAMP);
