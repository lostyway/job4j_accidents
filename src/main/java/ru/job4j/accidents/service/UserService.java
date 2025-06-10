package ru.job4j.accidents.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private PasswordEncoder encoder;

    public void register(String username, String password) {
        String encodedPassword = encoder.encode(password);
        jdbc.update(
                "insert into users (username, password, enabled) values (?, ?, true)",
                username, encodedPassword
        );
        jdbc.update(
                "insert into authorities(username, authority) values (?, 'USER')", username
        );
    }
}
