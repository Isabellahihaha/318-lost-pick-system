package com.csci318.found.web;

import com.csci318.found.domain.FoundItemPost;

import java.time.Instant;

/**
 * What the API returns for a found-item report.
 */
public class FoundItemResponse {

    private final Long id;
    private final String name;
    private final String category;
    private final String description;
    private final String location;
    private final String eventDate;
    private final String contactEmail;
    private final String ownerUsername;
    private final boolean resolved;
    private final Instant createdAt;

    public FoundItemResponse(Long id, String name, String category, String description, String location,
                              String eventDate, String contactEmail, String ownerUsername,
                              boolean resolved, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.contactEmail = contactEmail;
        this.ownerUsername = ownerUsername;
        this.resolved = resolved;
        this.createdAt = createdAt;
    }

    public static FoundItemResponse from(FoundItemPost entity) {
        return new FoundItemResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getLocation(),
                entity.getEventDate(),
                entity.getContactEmail(),
                entity.getOwnerUsername(),
                entity.isResolved(),
                entity.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public boolean isResolved() {
        return resolved;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
