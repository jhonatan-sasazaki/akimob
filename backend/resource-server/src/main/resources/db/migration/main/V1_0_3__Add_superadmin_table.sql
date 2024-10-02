CREATE TABLE IF NOT EXISTS superadmin (
    users_id BIGINT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT superadmin_users_id_fk FOREIGN KEY (users_id) REFERENCES users(users_id) ON DELETE CASCADE
);
