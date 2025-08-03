CREATE TABLE message (
    id SERIAL PRIMARY KEY,
    author_id INT REFERENCES media_user(id) NOT NULL,
    receiver_id INT REFERENCES media_user(id) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);
