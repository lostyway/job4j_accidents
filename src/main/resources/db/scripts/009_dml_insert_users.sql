-- Пользователь lostway с паролем 123 (BCrypt)
INSERT INTO users (username, password, enabled)
VALUES ('lostway', '$2a$10$x0x/XVoiQF7M55uJMXnJduc997dJRp/QavmG.LbfX0HbP7dkva84W', true);

-- Пользователь admin с паролем admin (BCrypt)
INSERT INTO users (username, password, enabled)
VALUES ('admin', '$2a$10$iXRVHNbzVvdkC56EwUK2zuwf8wlxpk3A9bYP2/hW8Lt7Wb/c08B1q', true);