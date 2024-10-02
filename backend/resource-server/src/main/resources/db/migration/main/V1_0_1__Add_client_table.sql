CREATE TABLE IF NOT EXISTS client (
    client_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    schema_name TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT client_name_uk UNIQUE (name),
    CONSTRAINT client_name_ck CHECK (char_length(name) BETWEEN 1 AND 255), -- 1-255 characters
    CONSTRAINT client_schema_name_uk UNIQUE (schema_name),
    CONSTRAINT client_schema_name_ck CHECK (char_length(schema_name) BETWEEN 1 AND 63) -- PostgreSQL schema name limit
);
