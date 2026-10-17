package com.lostandfound.aimatching.service;

import com.lostandfound.aimatching.client.FoundItemView;
import com.lostandfound.aimatching.client.FoundServiceClient;
import com.lostandfound.aimatching.client.LostItemView;
import com.lostandfound.aimatching.client.LostServiceClient;
import com.lostandfound.aimatching.dto.MatchCalculationRequest;
import com.lostandfound.aimatching.dto.MatchResponse;
import com.lostandfound.aimatching.event.FoundItemReportedEvent;
import com.lostandfound.aimatching.event.LostItemReportedEvent;
import com.lostandfound.aimatching.event.PotentialMatchFoundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * FR: Event-Driven Architecture + Stream Processing entry point for the
 * asynchronous matching path. Triggered by MatchingStreamConsumerConfig's
 * Consumer beans whenever a FoundItemReportedEvent or LostItemReportedEvent
 * arrives on Kafka. Reuses the exact same AiMatchingService.calculateMatchScore
 * logic that the existing synchronous REST flow (POST /api/ai-matching/calculate-match,
 * called directly by found-service/lost-service on item creation) already
 * uses, so the two paths can never disagree on what counts as a match.
 *
 * For every HIGH_CONFIDENCE or SUGGESTED result, publishes a
 * PotentialMatchFoundEvent to Kafka (topic "potential-match-found"), per
 * specs/5-event-sourcing.md.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MatchEventOrchestrator {

    private static final String POTENTIAL_MATCH_BINDING = "potentialMatchFound-out-0";
    private static final Set<String> PUBLISHABLE_STATUSES = Set.of("HIGH_CONFIDENCE", "SUGGESTED");
    private static final DateTimeFormatter EVENT_DATE_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private final LostServiceClient lostServiceClient;
    private final FoundServiceClient foundServiceClient;
    private final AiMatchingService aiMatchingService;
    private final StreamBridge streamBridge;

    public void onFoundItemReported(FoundItemReportedEvent event) {
        log.info("Consumed FoundItemReported for found item {}, scoring against unresolved lost items", event.getFoundItemId());
        List<LostItemView> candidates = lostServiceClient.listUnresolved();
        for (LostItemView lost : candidates) {
            MatchCalculationRequest request = new MatchCalculationRequest();
            request.setLostItemId(lost.getId());
            request.setFoundItemId(event.getFoundItemId());
            request.setLostItemDescription(lost.getDescription());
            request.setFoundItemDescription(event.getDescription());
            request.setLostCategory(lost.getCategory());
            request.setFoundCategory(event.getCategory());
            request.setLostLocation(lost.getLocation());
            request.setFoundLocation(event.getLocation());
            request.setLostDate(parseEventDate(lost.getEventDate()));
            request.setFoundDate(parseEventDate(event.getEventDate()));

            scoreAndPublish(request);
        }
    }

    public void onLostItemReported(LostItemReportedEvent event) {
        log.info("Consumed LostItemReported for lost item {}, scoring against unresolved found items", event.getLostItemId());
        List<FoundItemView> candidates = foundServiceClient.listUnresolved();
        for (FoundItemView found : candidates) {
            MatchCalculationRequest request = new MatchCalculationRequest();
            request.setLostItemId(event.getLostItemId());
            request.setFoundItemId(found.getId());
            request.setLostItemDescription(event.getDescription());
            request.setFoundItemDescription(found.getDescription());
            request.setLostCategory(event.getCategory());
            request.setFoundCategory(found.getCategory());
            request.setLostLocation(event.getLocation());
            request.setFoundLocation(found.getLocation());
            request.setLostDate(parseEventDate(event.getEventDate()));
            request.setFoundDate(parseEventDate(found.getEventDate()));

            scoreAndPublish(request);
        }
    }

    private void scoreAndPublish(MatchCalculationRequest request) {
        MatchResponse response = aiMatchingService.calculateMatchScore(request);
        if (response == null || !PUBLISHABLE_STATUSES.contains(response.getMatchStatus())) {
            return;
        }

        PotentialMatchFoundEvent event = new PotentialMatchFoundEvent(
                UUID.randomUUID().toString(),
                response.getLostItemId(),
                response.getFoundItemId(),
                response.getTotalConfidenceScore(),
                response.getMatchStatus(),
                response.getMatchReasoning(),
                Instant.now()
        );

        try {
            boolean sent = streamBridge.send(POTENTIAL_MATCH_BINDING, event);
            if (!sent) {
                log.warn("PotentialMatchFound event (lost {} / found {}) was not accepted by the Kafka binder",
                        event.getLostItemId(), event.getFoundItemId());
            } else {
                log.info("Published PotentialMatchFound: lost={} found={} status={} score={}",
                        event.getLostItemId(), event.getFoundItemId(), event.getMatchStatus(), event.getConfidenceScore());
            }
        } catch (Exception e) {
            log.warn("Could not publish PotentialMatchFound event (lost {} / found {}): {}",
                    event.getLostItemId(), event.getFoundItemId(), e.getMessage());
        }
    }

    private static LocalDateTime parseEventDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String normalized = raw.trim().replaceAll("(?i)\\bSept\\b", "Sep");
        try {
            return LocalDate.parse(normalized, EVENT_DATE_FORMAT).atStartOfDay();
        } catch (DateTimeParseException e) {
            log.warn("Could not parse event date '{}', temporal score will stay neutral: {}", raw, e.getMessage());
            return null;
        }
    }
}
