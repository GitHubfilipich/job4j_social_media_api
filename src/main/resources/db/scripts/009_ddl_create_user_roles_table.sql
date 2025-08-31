CREATE TABLE user_roles (
    user_id INT REFERENCES media_user(id) NOT NULL,
    role_id INT REFERENCES roles(id) NOT NULL,
    UNIQUE (user_id, role_id)
);
