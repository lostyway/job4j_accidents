CREATE TABLE users
(
    username VARCHAR(50)  NOT NULL,
    password text NOT NULL,
    enabled  boolean default true,
    PRIMARY KEY (username)
);