CREATE TABLE IF NOT EXISTS locality (
    locality_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    administrative_area_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by BIGINT DEFAULT NULL,

    CONSTRAINT locality_administrative_area_id_fk FOREIGN KEY (administrative_area_id) REFERENCES administrative_area (administrative_area_id),
    CONSTRAINT locality_name_ck CHECK (char_length(name) BETWEEN 1 AND 255),
    CONSTRAINT locality_created_by_fk FOREIGN KEY (created_by) REFERENCES client_user (client_user_id),
    CONSTRAINT locality_updated_by_fk FOREIGN KEY (updated_by) REFERENCES client_user (client_user_id)
);
