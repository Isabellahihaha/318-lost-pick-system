package com.csci318.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * A registered account on the platform.
 *
 * Deliberately does NOT hold its own found-posts / lost-requests as
 * relationships here: those entities live in found-service and
 * lost-service, each with its own separate database. There is no
 * cross-service foreign key in this architecture. A user's own posts
 * and requests are fetched on demand from those services' existing
 * "?owner=username" filters (see FoundItemPostController / LostItemRequestController),
 * never stored or duplicated on this entity.
 *
 * Password is stored as plain text for now, matching the project's
 * current placeholder-auth scope (no hashing/claims model yet). This
 * is a known gap to revisit, not an oversight.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(updatable = false)
    private Instant createdAt = Instant.now();

    protected User() {
        // required by JPA
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
