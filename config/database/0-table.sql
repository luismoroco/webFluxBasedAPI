CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    password TEXT,
    username TEXT
);

INSERT INTO users (name, password, username) VALUES ('User 1', 'password1', 'user1');
INSERT INTO users (name, password, username) VALUES ('User 2', 'password2', 'user2');
INSERT INTO users (name, password, username) VALUES ('User 3', 'password3', 'user3');
INSERT INTO users (name, password, username) VALUES ('User 4', 'password4', 'user4');
INSERT INTO users (name, password, username) VALUES ('User 5', 'password5', 'user5');
INSERT INTO users (name, password, username) VALUES ('User 6', 'password6', 'user6');