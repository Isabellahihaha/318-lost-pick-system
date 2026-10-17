package com.lostandfound.aimatching.client;

import lombok.Data;

import java.time.Instant;

@Data
public class FoundItemView {
    private Long id;
    private String name;
    private String category;
    private String description;
    private String location;
    private String eventDate;
    private String contactEmail;
    private String ownerUsername;
    private boolean resolved;
    private Instant createdAt;
}
