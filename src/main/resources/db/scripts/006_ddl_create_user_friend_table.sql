CREATE TABLE user_friend (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES media_user(id) NOT NULL,
    friend_id INT REFERENCES media_user(id) NOT NULL,
    UNIQUE (user_id, friend_id)
);
