CREATE TABLE IF NOT EXISTS role_group (
    role_group_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    description TEXT,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by BIGINT DEFAULT NULL,

    CONSTRAINT role_group_name_uk UNIQUE (name),
    CONSTRAINT role_group_name_ck CHECK (char_length(name) BETWEEN 1 AND 255), -- 1-255 characters
    CONSTRAINT role_group_description_ck CHECK (char_length(description) BETWEEN 0 AND 255), -- 0-255 characters
    CONSTRAINT role_group_created_by_fk FOREIGN KEY (created_by) REFERENCES client_user (client_user_id),
    CONSTRAINT role_group_updated_by_fk FOREIGN KEY (updated_by) REFERENCES client_user (client_user_id)
);

ALTER TABLE client_user ADD COLUMN role_group_id BIGINT NOT NULL;
ALTER TABLE client_user ADD CONSTRAINT client_user_role_group_id_fk FOREIGN KEY (role_group_id) REFERENCES role_group (role_group_id);
