CREATE TABLE user_subscription (
    id SERIAL PRIMARY KEY,
    subscriber_id INT REFERENCES media_user(id) NOT NULL,
    publisher_id INT REFERENCES media_user(id) NOT NULL,
    UNIQUE (subscriber_id, publisher_id)
);
