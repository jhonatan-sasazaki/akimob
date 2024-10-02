CREATE TYPE person_type AS ENUM ('NATURAL', 'LEGAL');

CREATE TABLE IF NOT EXISTS person (
    person_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    type person_type NOT NULL,
    address_id BIGINT,
    phone_number TEXT,
    email TEXT,
    photo_url TEXT,
    observation TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    updated_by BIGINT,

    CONSTRAINT person_address_id_fk FOREIGN KEY (address_id) REFERENCES address (address_id),
    CONSTRAINT person_created_by_fk FOREIGN KEY (created_by) REFERENCES client_user (client_user_id),
    CONSTRAINT person_updated_by_fk FOREIGN KEY (updated_by) REFERENCES client_user (client_user_id),
    CONSTRAINT person_name_ck CHECK (char_length(name) BETWEEN 1 AND 255),
    CONSTRAINT person_phone_number_ck CHECK (char_length(phone_number) BETWEEN 1 AND 255),
    CONSTRAINT person_email_ck CHECK (char_length(email) BETWEEN 1 AND 255),
    CONSTRAINT person_photo_url_ck CHECK (char_length(photo_url) BETWEEN 1 AND 255),
    CONSTRAINT person_observation_ck CHECK (char_length(observation) BETWEEN 1 AND 255)
);

CREATE TYPE person_gender AS ENUM ('MALE', 'FEMALE', 'OTHER');

CREATE TABLE IF NOT EXISTS natural_person (
    person_id BIGINT PRIMARY KEY,
    personal_identifier TEXT, -- CPF
    gender person_gender,
    birthdate DATE,

    CONSTRAINT natural_person_person_id_fk FOREIGN KEY (person_id) REFERENCES person (person_id) ON DELETE CASCADE,
    CONSTRAINT natural_person_personal_identifier_ck CHECK (char_length(personal_identifier) BETWEEN 1 AND 255)
);

CREATE TABLE IF NOT EXISTS legal_entity (
    person_id BIGINT PRIMARY KEY,
    business_identifier TEXT, -- CNPJ
    registered_name TEXT,

    CONSTRAINT legal_entity_person_id_fk FOREIGN KEY (person_id) REFERENCES person (person_id) ON DELETE CASCADE,
    CONSTRAINT legal_entity_business_identifier_ck CHECK (char_length(business_identifier) BETWEEN 1 AND 255),
    CONSTRAINT legal_entity_registered_name_ck CHECK (char_length(registered_name) BETWEEN 1 AND 255)
);
