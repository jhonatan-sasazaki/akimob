CREATE TABLE client_users (
    client_users_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id BIGINT,
    users_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT client_users_uk UNIQUE (client_id, users_id),
    CONSTRAINT client_users_client_id_fk FOREIGN KEY (client_id) REFERENCES client(client_id) ON DELETE CASCADE,
    CONSTRAINT client_users_users_id_fk FOREIGN KEY (users_id) REFERENCES users(users_id) ON DELETE CASCADE
);
