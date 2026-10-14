package com.csci318.user.service;

import com.csci318.user.domain.User;
import com.csci318.user.repository.UserRepository;
import com.csci318.user.web.CreateUserRequest;
import com.csci318.user.web.LoginRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(CreateUserRequest payload) {
        requireText(payload.getUsername(), "username");
        requireText(payload.getEmail(), "email");
        requireText(payload.getPassword(), "password");

        String username = payload.getUsername().trim();
        String email = payload.getEmail().trim();

        if (repository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User entity = new User(username, email, payload.getPassword());
        return repository.save(entity);
    }

    public User login(LoginRequest payload) {
        requireText(payload.getUsername(), "username");
        requireText(payload.getPassword(), "password");

        User user = repository.findByUsernameIgnoreCase(payload.getUsername().trim())
                .orElseThrow(() -> new NoSuchElementException("No account with that username"));

        if (!user.getPassword().equals(payload.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }
        return user;
    }

    public User get(String username) {
        return repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NoSuchElementException("No account with that username"));
    }

    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }
}
