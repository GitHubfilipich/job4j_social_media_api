CREATE TABLE friend_request (
    id SERIAL PRIMARY KEY,
    author_id INT REFERENCES media_user(id) NOT NULL,
    receiver_id INT REFERENCES media_user(id) NOT NULL,
    status TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    UNIQUE (author_id, receiver_id)
);
