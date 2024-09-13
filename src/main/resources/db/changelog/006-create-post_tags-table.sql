--liquibase formatted sql
--changeset Murad:006-create-post_tags-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
CREATE TABLE IF NOT EXISTS post_schema.post_tags (
    post_id BIGINT,
    tag_id BIGINT,
    CONSTRAINT fk_post_tag FOREIGN KEY (post_id) REFERENCES post(id),
    CONSTRAINT fk_tag_post FOREIGN KEY (tag_id) REFERENCES tag(id),
    PRIMARY KEY (post_id, tag_id)
);

-- Вставка тестовых данных для связи post и tag
INSERT INTO post_tags (post_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 1);




