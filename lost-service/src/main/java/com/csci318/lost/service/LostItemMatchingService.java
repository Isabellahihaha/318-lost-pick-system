package com.csci318.lost.service;

import com.csci318.lost.client.FoundItemView;
import com.csci318.lost.client.FoundServiceClient;
import com.csci318.lost.client.MatchCalculationRequestDto;
import com.csci318.lost.client.MatchResponseDto;
import com.csci318.lost.client.MatchingServiceClient;
import com.csci318.lost.domain.LostItemRequest;
import com.csci318.lost.web.MatchCandidateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class LostItemMatchingService {

    private static final Logger log = LoggerFactory.getLogger(LostItemMatchingService.class);

    private static final DateTimeFormatter EVENT_DATE_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private final FoundServiceClient foundServiceClient;
    private final MatchingServiceClient matchingServiceClient;

    public LostItemMatchingService(FoundServiceClient foundServiceClient,
                                    MatchingServiceClient matchingServiceClient) {
        this.foundServiceClient = foundServiceClient;
        this.matchingServiceClient = matchingServiceClient;
    }

    public List<MatchCandidateResponse> findMatches(LostItemRequest lostItem) {
        List<FoundItemView> candidates = foundServiceClient.listUnresolved();

        return candidates.stream()
                .map(found -> score(lostItem, found))
                .filter(scored -> scored != null && isWorthShowing(scored.response().getMatchStatus()))
                .sorted(Comparator.comparingDouble(
                        (ScoredCandidate sc) -> sc.response().getTotalConfidenceScore()).reversed())
                .map(this::toResponse)
                .toList();
    }

    private static boolean isWorthShowing(String matchStatus) {
        return "HIGH_CONFIDENCE".equals(matchStatus) || "SUGGESTED".equals(matchStatus);
    }

    private ScoredCandidate score(LostItemRequest lostItem, FoundItemView found) {
        MatchCalculationRequestDto request = new MatchCalculationRequestDto();
        request.setLostItemId(lostItem.getId());
        request.setFoundItemId(found.getId());
        request.setLostItemDescription(lostItem.getDescription());
        request.setFoundItemDescription(found.getDescription());
        request.setLostCategory(lostItem.getCategory());
        request.setFoundCategory(found.getCategory());
        request.setLostLocation(lostItem.getLocation());
        request.setFoundLocation(found.getLocation());
        request.setLostDate(parseEventDate(lostItem.getEventDate()));
        request.setFoundDate(parseEventDate(found.getEventDate()));

        MatchResponseDto response = matchingServiceClient.calculateMatch(request);
        if (response == null) {
            return null;
        }
        return new ScoredCandidate(found, response);
    }

    private MatchCandidateResponse toResponse(ScoredCandidate scored) {
        FoundItemView found = scored.found();
        MatchResponseDto response = scored.response();
        return new MatchCandidateResponse(
                found.getId(),
                found.getName(),
                found.getDescription(),
                found.getLocation(),
                found.getEventDate(),
                found.getContactEmail(),
                found.getCategory(),
                found.getOwnerUsername(),
                response.getTotalConfidenceScore(),
                response.getTextSimilarityScore(),
                response.getSpatialProximityScore(),
                response.getTemporalProximityScore(),
                response.getMatchStatus(),
                response.getMatchReasoning()
        );
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

    private record ScoredCandidate(FoundItemView found, MatchResponseDto response) {
    }
}
