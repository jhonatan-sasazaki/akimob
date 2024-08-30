CREATE TABLE IF NOT EXISTS superadmin (
    user_account_id BIGINT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,
    
    CONSTRAINT superadmin_user_account_id_fk FOREIGN KEY (user_account_id) REFERENCES user_account(user_account_id) ON DELETE CASCADE
);
