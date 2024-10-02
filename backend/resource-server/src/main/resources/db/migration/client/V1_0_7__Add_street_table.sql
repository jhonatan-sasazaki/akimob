CREATE TABLE IF NOT EXISTS street (
    street_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    locality_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by BIGINT DEFAULT NULL,

    CONSTRAINT street_locality_id_fk FOREIGN KEY (locality_id) REFERENCES locality (locality_id),
    CONSTRAINT street_name_ck CHECK (char_length(name) BETWEEN 1 AND 255),
    CONSTRAINT street_created_by_fk FOREIGN KEY (created_by) REFERENCES client_user (client_user_id),
    CONSTRAINT street_updated_by_fk FOREIGN KEY (updated_by) REFERENCES client_user (client_user_id)
);
