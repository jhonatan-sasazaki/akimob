CREATE TABLE IF NOT EXISTS country (
    country_id SMALLINT NOT NULL,
    iso2 TEXT NOT NULL,
    name TEXT NOT NULL,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by BIGINT DEFAULT NULL,

    CONSTRAINT country_country_id_pk PRIMARY KEY (country_id),
    CONSTRAINT country_iso2_ck CHECK (iso2 ~ '^[A-Z]{2}$'), -- 2 uppercase letters
    CONSTRAINT country_name_ck CHECK (char_length(name) BETWEEN 1 AND 255),
    CONSTRAINT country_created_by_fk FOREIGN KEY (created_by) REFERENCES client_user (client_user_id),
    CONSTRAINT country_updated_by_fk FOREIGN KEY (updated_by) REFERENCES client_user (client_user_id)
);
