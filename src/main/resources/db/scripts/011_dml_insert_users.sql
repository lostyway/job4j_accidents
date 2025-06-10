-- Пользователь lostway с паролем 123 (BCrypt)
INSERT INTO users (username, password, enabled, authority_id)
VALUES ('lostway', '$2a$10$x0x/XVoiQF7M55uJMXnJduc997dJRp/QavmG.LbfX0HbP7dkva84W', true, 1);

-- Пользователь admin с паролем admin (BCrypt)
INSERT INTO users (username, password, enabled, authority_id)
VALUES ('admin', '$2a$10$iXRVHNbzVvdkC56EwUK2zuwf8wlxpk3A9bYP2/hW8Lt7Wb/c08B1q', true, 2);