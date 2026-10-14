package com.csci318.user.web;

import com.csci318.user.domain.User;

import java.time.Instant;

/**
 * What the API returns for an account. Deliberately excludes the
 * password field — never echo it back to the frontend.
 */
public class UserResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final Instant createdAt;

    public UserResponse(Long id, String username, String email, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }

    public static UserResponse from(User entity) {
        return new UserResponse(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getCreatedAt());
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
