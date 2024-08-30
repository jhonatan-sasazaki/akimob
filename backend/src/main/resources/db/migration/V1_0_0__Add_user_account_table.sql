CREATE TABLE IF NOT EXISTS user_account (
    user_account_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    email TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT user_account_username_uk UNIQUE (username),
    CONSTRAINT user_account_email_uk UNIQUE (email),
    CONSTRAINT user_account_username_ck CHECK (username ~ '^[^\s]{1,255}$') -- 1-255 characters, no spaces
);
