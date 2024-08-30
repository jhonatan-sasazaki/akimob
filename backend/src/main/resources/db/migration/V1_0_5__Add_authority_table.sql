CREATE TABLE IF NOT EXISTS authority (
    authority_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT authority_name_uk UNIQUE (name),
    CONSTRAINT authority_name_ck CHECK (name ~ '^[^\s]{1,255}$') -- 1-255 characters, no spaces
);
