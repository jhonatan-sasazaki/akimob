CREATE TABLE IF NOT EXISTS authority (
    authority_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by BIGINT DEFAULT NULL,

    CONSTRAINT authority_name_uk UNIQUE (name),
    CONSTRAINT authority_name_ck CHECK (name ~ '^[\S]{1,255}$'), -- 1-255 characters, no spaces
    CONSTRAINT authority_description_ck CHECK (char_length(description) BETWEEN 0 AND 255), -- 0-255 characters
    CONSTRAINT authority_created_by_fk FOREIGN KEY (created_by) REFERENCES client_user (client_user_id),
    CONSTRAINT authority_updated_by_fk FOREIGN KEY (updated_by) REFERENCES client_user (client_user_id)
);
