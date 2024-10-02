CREATE TABLE IF NOT EXISTS address (
    address_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    street_id BIGINT NOT NULL,
    address_number TEXT NOT NULL,
    complement TEXT DEFAULT NULL,
    postal_code TEXT NOT NULL,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by BIGINT DEFAULT NULL,

    CONSTRAINT address_street_id_fk FOREIGN KEY (street_id) REFERENCES street (street_id),
    CONSTRAINT address_address_number_ck CHECK (char_length(address_number) BETWEEN 1 AND 255),
    CONSTRAINT address_complement_ck CHECK (char_length(complement) BETWEEN 1 AND 255),
    CONSTRAINT address_postal_code_ck CHECK (char_length(postal_code) BETWEEN 1 AND 255),
    CONSTRAINT address_created_by_fk FOREIGN KEY (created_by) REFERENCES client_user (client_user_id),
    CONSTRAINT address_updated_by_fk FOREIGN KEY (updated_by) REFERENCES client_user (client_user_id)
);
