package com.csci318.lost.event;

import java.time.Instant;

/**
 * FR: Event-Driven Architecture - published to Kafka (topic "lost-item-reported")
 * whenever a new lost-item request is created, so Matching-Service can react
 * asynchronously instead of only being reachable via the existing synchronous
 * REST matching call.
 */
public class LostItemReportedEvent {

    private String eventId;
    private Long lostItemId;
    private String name;
    private String category;
    private String description;
    private String location;
    private String eventDate;
    private String contactEmail;
    private String ownerUsername;
    private Instant occurredAt;

    public LostItemReportedEvent() {
    }

    public LostItemReportedEvent(String eventId, Long lostItemId, String name, String category,
                                  String description, String location, String eventDate,
                                  String contactEmail, String ownerUsername, Instant occurredAt) {
        this.eventId = eventId;
        this.lostItemId = lostItemId;
        this.name = name;
        this.category = category;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.contactEmail = contactEmail;
        this.ownerUsername = ownerUsername;
        this.occurredAt = occurredAt;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(Long lostItemId) {
        this.lostItemId = lostItemId;
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

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }
}
