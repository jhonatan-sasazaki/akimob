CREATE TABLE IF NOT EXISTS role_group (
    role_group_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT role_group_role_group_id_client_id_uk UNIQUE (role_group_id, client_id),
    CONSTRAINT role_group_client_id_name_uk UNIQUE (client_id, name),
    CONSTRAINT role_group_name_ck CHECK (char_length(name) BETWEEN 1 AND 255), -- 1-255 characters
    CONSTRAINT role_group_client_id_fk FOREIGN KEY (client_id) REFERENCES client(client_id) ON DELETE CASCADE
);
