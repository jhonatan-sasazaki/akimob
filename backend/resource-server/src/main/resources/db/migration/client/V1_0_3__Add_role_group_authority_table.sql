CREATE TABLE IF NOT EXISTS role_group_authority (
    role_group_id BIGINT NOT NULL,
    authority_id BIGINT NOT NULL,

    CONSTRAINT role_group_authority_pk PRIMARY KEY (role_group_id, authority_id),
    CONSTRAINT role_group_authority_role_group_id_fk FOREIGN KEY (role_group_id) REFERENCES role_group (role_group_id) ON DELETE CASCADE,
    CONSTRAINT role_group_authority_authority_id_fk FOREIGN KEY (authority_id) REFERENCES authority (authority_id) ON DELETE CASCADE
);
