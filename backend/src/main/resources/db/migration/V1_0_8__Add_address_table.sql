CREATE TABLE IF NOT EXISTS country (
    country_id SMALLINT NOT NULL,
    iso2 TEXT NOT NULL,
    name TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT country_country_id_pk PRIMARY KEY (country_id),
    CONSTRAINT country_iso2_ck CHECK (iso2 ~ '^[A-Z]{2}$'), -- 2 uppercase letters
    CONSTRAINT country_name_ck CHECK (char_length(name) BETWEEN 1 AND 255)
);

CREATE TABLE IF NOT EXISTS administrative_area (
    administrative_area_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    country_id SMALLINT NOT NULL,
    name TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,

    CONSTRAINT administrative_area_country_id_fk FOREIGN KEY (country_id) REFERENCES country (country_id),
    CONSTRAINT administrative_area_name_ck CHECK (char_length(name) BETWEEN 1 AND 255),
    CONSTRAINT administrative_area_created_by_fk FOREIGN KEY (created_by) REFERENCES user_account (user_account_id)
);

CREATE TABLE IF NOT EXISTS locality (
    locality_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    administrative_area_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,

    CONSTRAINT locality_administrative_area_id_fk FOREIGN KEY (administrative_area_id) REFERENCES administrative_area (administrative_area_id),
    CONSTRAINT locality_name_ck CHECK (char_length(name) BETWEEN 1 AND 255),
    CONSTRAINT locality_created_by_fk FOREIGN KEY (created_by) REFERENCES user_account (user_account_id)
);

CREATE TABLE IF NOT EXISTS street (
    street_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    locality_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,

    CONSTRAINT street_locality_id_fk FOREIGN KEY (locality_id) REFERENCES locality (locality_id),
    CONSTRAINT street_name_ck CHECK (char_length(name) BETWEEN 1 AND 255),
    CONSTRAINT street_created_by_fk FOREIGN KEY (created_by) REFERENCES user_account (user_account_id)
);

CREATE TABLE IF NOT EXISTS address (
    address_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    street_id BIGINT NOT NULL,
    address_number TEXT NOT NULL,
    complement TEXT DEFAULT NULL,
    postal_code TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,

    CONSTRAINT address_street_id_fk FOREIGN KEY (street_id) REFERENCES street (street_id),
    CONSTRAINT address_address_number_ck CHECK (char_length(address_number) BETWEEN 1 AND 255),
    CONSTRAINT address_complement_ck CHECK (char_length(complement) BETWEEN 1 AND 255),
    CONSTRAINT address_postal_code_ck CHECK (char_length(postal_code) BETWEEN 1 AND 255),
    CONSTRAINT address_created_by_fk FOREIGN KEY (created_by) REFERENCES user_account (user_account_id)
);
