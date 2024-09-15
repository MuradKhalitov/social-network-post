--liquibase formatted sql
--changeset Murad:007-create-photo-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
CREATE TABLE IF NOT EXISTS post_schema.photo (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    image_path VARCHAR(255)
);




