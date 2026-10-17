package com.csci318.found.service;

import com.csci318.found.client.LostItemView;
import com.csci318.found.client.LostServiceClient;
import com.csci318.found.client.MatchCalculationRequestDto;
import com.csci318.found.client.MatchResponseDto;
import com.csci318.found.client.MatchingServiceClient;
import com.csci318.found.domain.FoundItemPost;
import com.csci318.found.web.MatchCandidateResponse;
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
public class FoundItemMatchingService {

    private static final Logger log = LoggerFactory.getLogger(FoundItemMatchingService.class);

    private static final DateTimeFormatter EVENT_DATE_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private final LostServiceClient lostServiceClient;
    private final MatchingServiceClient matchingServiceClient;

    public FoundItemMatchingService(LostServiceClient lostServiceClient,
                                     MatchingServiceClient matchingServiceClient) {
        this.lostServiceClient = lostServiceClient;
        this.matchingServiceClient = matchingServiceClient;
    }

    public List<MatchCandidateResponse> findMatches(FoundItemPost foundItem) {
        List<LostItemView> candidates = lostServiceClient.listUnresolved();

        return candidates.stream()
                .map(lost -> score(foundItem, lost))
                .filter(scored -> scored != null && isWorthShowing(scored.response().getMatchStatus()))
                .sorted(Comparator.comparingDouble(
                        (ScoredCandidate sc) -> sc.response().getTotalConfidenceScore()).reversed())
                .map(this::toResponse)
                .toList();
    }

    private static boolean isWorthShowing(String matchStatus) {
        return "HIGH_CONFIDENCE".equals(matchStatus) || "SUGGESTED".equals(matchStatus);
    }

    private ScoredCandidate score(FoundItemPost foundItem, LostItemView lost) {
        MatchCalculationRequestDto request = new MatchCalculationRequestDto();
        request.setLostItemId(lost.getId());
        request.setFoundItemId(foundItem.getId());
        request.setLostItemDescription(lost.getDescription());
        request.setFoundItemDescription(foundItem.getDescription());
        request.setLostCategory(lost.getCategory());
        request.setFoundCategory(foundItem.getCategory());
        request.setLostLocation(lost.getLocation());
        request.setFoundLocation(foundItem.getLocation());
        request.setLostDate(parseEventDate(lost.getEventDate()));
        request.setFoundDate(parseEventDate(foundItem.getEventDate()));

        MatchResponseDto response = matchingServiceClient.calculateMatch(request);
        if (response == null) {
            return null;
        }
        return new ScoredCandidate(lost, response);
    }

    private MatchCandidateResponse toResponse(ScoredCandidate scored) {
        LostItemView lost = scored.lost();
        MatchResponseDto response = scored.response();
        return new MatchCandidateResponse(
                lost.getId(),
                lost.getName(),
                lost.getDescription(),
                lost.getLocation(),
                lost.getEventDate(),
                lost.getContactEmail(),
                lost.getCategory(),
                lost.getOwnerUsername(),
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

    private record ScoredCandidate(LostItemView lost, MatchResponseDto response) {
    }
}
