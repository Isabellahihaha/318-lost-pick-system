package com.lostandfound.aimatching.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * FR: Event-Driven Architecture - published to Kafka (topic
 * "potential-match-found") whenever the asynchronous matching path
 * (triggered by a consumed FoundItemReportedEvent / LostItemReportedEvent)
 * finds a HIGH_CONFIDENCE or SUGGESTED match, per specs/5-event-sourcing.md.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PotentialMatchFoundEvent {
    private String matchId;
    private Long lostItemId;
    private Long foundItemId;
    private Double confidenceScore;
    private String matchStatus;
    private String matchReasoning;
    private Instant occurredAt;
}
