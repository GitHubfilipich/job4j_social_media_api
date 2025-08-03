CREATE TABLE image (
    id SERIAL PRIMARY KEY,
    post_id INT REFERENCES post(id) NOT NULL,
    name TEXT NOT NULL,
    url TEXT NOT NULL
);
