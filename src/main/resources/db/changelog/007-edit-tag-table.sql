--liquibase formatted sql
--changeset Murad:007-edit-tag-table.sql
--preconditions onFail:CONTINUE onError:CONTINUE
ALTER TABLE tag
ADD CONSTRAINT tag_name_unique UNIQUE (name);



