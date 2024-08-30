CREATE TABLE IF NOT EXISTS role_group_user_account (
    role_group_id BIGINT NOT NULL,
    user_account_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT role_group_user_account_pk PRIMARY KEY (role_group_id, user_account_id),
    CONSTRAINT role_group_user_account_role_group_id_fk FOREIGN KEY (role_group_id) REFERENCES role_group (role_group_id) ON DELETE CASCADE,
    CONSTRAINT role_group_user_account_user_account_id_fk FOREIGN KEY (user_account_id) REFERENCES user_account (user_account_id) ON DELETE CASCADE
);
