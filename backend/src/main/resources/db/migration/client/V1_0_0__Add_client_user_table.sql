CREATE TABLE IF NOT EXISTS client_user (
    client_user_id BIGINT PRIMARY KEY,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by BIGINT DEFAULT NULL,

    CONSTRAINT client_user_created_by_fk FOREIGN KEY (created_by) REFERENCES client_user (client_user_id),
    CONSTRAINT client_user_updated_by_fk FOREIGN KEY (updated_by) REFERENCES client_user (client_user_id)
);
