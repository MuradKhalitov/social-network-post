--liquibase formatted sql
--changeset Murad:007-add-columns-to-like_post

-- Добавление колонки 'type'
ALTER TABLE post_schema.like_post
ADD COLUMN type VARCHAR(20);

-- Добавление колонки 'reaction_type'
ALTER TABLE post_schema.like_post
ADD COLUMN reaction_type VARCHAR(20);
