package com.lostandfound.aimatching.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lostandfound.aimatching.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiMatchingService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key=YOUR_GEMINI_API_KEY}")
    private String apiKey;

    @Value("${openai.api.url=https://generativelanguage.googleapis.com/v1beta/openai/chat/completions}")
    private String apiUrl;

    /**
     * FR08: Extract structured item properties from natural language text
     */
    public ExtractedItemAttributes extractAttributes(String description) {
        if ("mock-key".equals(apiKey)) {
            return mockExtraction(description);
        }

        String prompt = """
            Extract structured fields from this lost/found report description. 
            Return JSON format with fields: itemName, category, primaryColor, brand, location, approximateDate, distinctiveFeatures (array of strings), serialNumber.
            Description: "%s"
            """.formatted(description);

        try {
            String jsonResponse = callLlmApi(prompt);
            return objectMapper.readValue(jsonResponse, ExtractedItemAttributes.class);
        } catch (Exception e) {
            log.error("Failed to extract attributes via LLM, falling back to basic extraction", e);
            return mockExtraction(description);
        }
    }

    /**
     * Identify missing information and ask follow-up questions
     */
    public FollowUpResponse generateFollowUps(FollowUpRequest request) {
        List<String> questions = new ArrayList<>();
        ExtractedItemAttributes attrs = request.getCurrentAttributes();

        if (attrs.getBrand() == null || attrs.getBrand().isBlank()) {
            questions.add("Do you know the brand or manufacturer of the item?");
        }
        if (attrs.getPrimaryColor() == null || attrs.getPrimaryColor().isBlank()) {
            questions.add("What primary color or distinctive colors does the item have?");
        }
        if (attrs.getSerialNumber() == null || attrs.getSerialNumber().isBlank()) {
            if ("Electronics".equalsIgnoreCase(attrs.getCategory())) {
                questions.add("Does the electronic device have a serial number, IMEI, or specific engraving?");
            }
        }
        if (attrs.getDistinctiveFeatures() == null || attrs.getDistinctiveFeatures().isEmpty()) {
            questions.add("Are there any unique stickers, scratches, keychains, or damage that distinguish it?");
        }

        return new FollowUpResponse(questions, !questions.isEmpty());
    }

    /**
     * Analyze lost and found reports and compute composite match score
     */
    public MatchResponse calculateMatchScore(MatchCalculationRequest req) {
        // 1. Text / Category Similarity (Weight: 50%)
        double textScore = computeTextSimilarity(req.getLostItemDescription(), req.getFoundItemDescription());
        if (req.getLostCategory() != null && req.getLostCategory().equalsIgnoreCase(req.getFoundCategory())) {
            textScore = Math.min(1.0, textScore + 0.2);
        }

        // 2. Spatial Distance Scoring (Weight: 30%)
        double spatialScore = computeSpatialScore(
                req.getLostLatitude(), req.getLostLongitude(),
                req.getFoundLatitude(), req.getFoundLongitude()
        );

        // 3. Temporal Distance Scoring (Weight: 20%)
        double temporalScore = computeTemporalScore(req.getLostDate(), req.getFoundDate());

        // Composite Weighted Score
        double overallScore = (textScore * 0.50) + (spatialScore * 0.30) + (temporalScore * 0.20);

        String status;
        if (overallScore >= 0.80) {
            status = "HIGH_CONFIDENCE";
        } else if (overallScore >= 0.50) {
            status = "SUGGESTED";
        } else {
            status = "LOW_PROBABILITY";
        }

        return MatchResponse.builder()
                .lostItemId(req.getLostItemId())
                .foundItemId(req.getFoundItemId())
                .totalConfidenceScore(Math.round(overallScore * 100.0) / 100.0)
                .textSimilarityScore(Math.round(textScore * 100.0) / 100.0)
                .spatialProximityScore(Math.round(spatialScore * 100.0) / 100.0)
                .temporalProximityScore(Math.round(temporalScore * 100.0) / 100.0)
                .matchStatus(status)
                .build();
    }

    // Helper: Jaccard Similarity for basic text matching
    private double computeTextSimilarity(String t1, String t2) {
        if (t1 == null || t2 == null) return 0.0;
        Set<String> words1 = new HashSet<>(Arrays.asList(t1.toLowerCase().split("\\W+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(t2.toLowerCase().split("\\W+")));

        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);

        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);

        if (union.isEmpty()) return 0.0;
        return (double) intersection.size() / union.size();
    }

    // Helper: Haversine distance scoring
    private double computeSpatialScore(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) return 0.5; // Default neutral score

        final int R = 6371; // Earth radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceKm = R * c;

        if (distanceKm <= 0.5) return 1.0;
        if (distanceKm <= 2.0) return 0.8;
        if (distanceKm <= 5.0) return 0.5;
        if (distanceKm <= 10.0) return 0.2;
        return 0.0;
    }

    // Helper: Time gap decay scoring
    private double computeTemporalScore(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) return 0.5;

        long hoursApart = Math.abs(Duration.between(d1, d2).toHours());
        if (hoursApart <= 6) return 1.0;
        if (hoursApart <= 24) return 0.85;
        if (hoursApart <= 72) return 0.60;
        if (hoursApart <= 168) return 0.30; // Within 1 week
        return 0.10;
    }

    private String callLlmApi(String prompt) {
    // Append API key as query param
    String urlWithKey = apiUrl + "?key=" + apiKey;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Request payload matching Gemini API structure
    Map<String, Object> body = Map.of(
        "contents", List.of(
            Map.of("parts", List.of(
                Map.of("text", "You are an AI assistant that extracts structured JSON data from text.\n\n" + prompt)
            ))
        ),
        "generationConfig", Map.of(
            "responseMimeType", "application/json"
        )
    );

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
    ResponseEntity<Map> response = restTemplate.postForEntity(urlWithKey, entity, Map.class);

    if (response.getBody() != null && response.getBody().containsKey("candidates")) {
        List candidates = (List) response.getBody().get("candidates");
        Map firstCandidate = (Map) candidates.get(0);
        Map content = (Map) firstCandidate.get("content");
        List parts = (List) content.get("parts");
        Map firstPart = (Map) parts.get(0);
        return (String) firstPart.get("text");
    }
    throw new RuntimeException("Empty response from Gemini API");
}

    private ExtractedItemAttributes mockExtraction(String text) {
        String lower = text.toLowerCase();
        return ExtractedItemAttributes.builder()
                .itemName(lower.contains("airpods") ? "AirPods Pro" : "Item")
                .category(lower.contains("airpods") || lower.contains("phone") ? "Electronics" : "General")
                .primaryColor(lower.contains("pink") ? "Pink" : lower.contains("white") ? "White" : "Unknown")
                .brand(lower.contains("apple") || lower.contains("airpods") ? "Apple" : "Generic")
                .location(lower.contains("library") ? "Library" : "Unknown")
                .distinctiveFeatures(List.of("Case attached"))
                .build();
    }
}
