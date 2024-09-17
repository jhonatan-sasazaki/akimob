CREATE TABLE IF NOT EXISTS administrative_area (
    administrative_area_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    country_id SMALLINT NOT NULL,
    name TEXT NOT NULL,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by BIGINT DEFAULT NULL,

    CONSTRAINT administrative_area_country_id_fk FOREIGN KEY (country_id) REFERENCES country (country_id),
    CONSTRAINT administrative_area_name_ck CHECK (char_length(name) BETWEEN 1 AND 255),
    CONSTRAINT administrative_area_created_by_fk FOREIGN KEY (created_by) REFERENCES client_user (client_user_id),
    CONSTRAINT administrative_area_updated_by_fk FOREIGN KEY (updated_by) REFERENCES client_user (client_user_id)
);
