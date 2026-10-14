package com.csci318.found.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * A single found-item report: "I found this, is it yours?"
 * This is the entity behind the Platform feed.
 */
@Entity
@Table(name = "found_item_posts")
public class FoundItemPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;

    @Column(length = 2000)
    private String description;

    private String location;

    /** Free-text date the item was found, e.g. "14 Sep 2026" — kept as text per the project's data spec. */
    private String eventDate;

    private String contactEmail;

    private String ownerUsername;

    private boolean resolved = false;

    @Column(updatable = false)
    private Instant createdAt = Instant.now();

    protected FoundItemPost() {
        // required by JPA
    }

    public FoundItemPost(String name, String category, String description, String location,
                          String eventDate, String contactEmail, String ownerUsername) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.contactEmail = contactEmail;
        this.ownerUsername = ownerUsername;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
