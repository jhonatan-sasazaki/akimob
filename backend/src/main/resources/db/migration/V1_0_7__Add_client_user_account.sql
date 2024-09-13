CREATE TABLE client_user_account (
    client_id BIGINT,
    user_account_id BIGINT,
    role_group_id BIGINT NOT NULL,
    
    CONSTRAINT client_user_account_pk PRIMARY KEY (client_id, user_account_id),
    CONSTRAINT client_user_account_client_id_fk FOREIGN KEY (client_id) REFERENCES client(client_id) ON DELETE CASCADE,
    CONSTRAINT client_user_account_user_account_id_fk FOREIGN KEY (user_account_id) REFERENCES user_account(user_account_id) ON DELETE CASCADE,
    CONSTRAINT client_user_account_role_group_id_fk FOREIGN KEY (role_group_id, client_id) REFERENCES role_group(role_group_id, client_id)
);
