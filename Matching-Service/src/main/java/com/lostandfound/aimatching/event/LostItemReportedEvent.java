package com.lostandfound.aimatching.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
