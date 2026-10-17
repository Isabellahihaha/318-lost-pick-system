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

    @Value("${openai.api.key:mock-key}")
    private String apiKey;

    @Value("${openai.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent}")
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

    public MatchResponse calculateMatchScore(MatchCalculationRequest req) {

        boolean haveBothDescriptions = req.getLostItemDescription() != null && req.getFoundItemDescription() != null;
        double textScore;
        String textReasoning;
        if (haveBothDescriptions && !"mock-key".equals(apiKey)) {
            try {
                AiTextSimilarity ai = computeTextSimilarityAi(req.getLostItemDescription(), req.getFoundItemDescription());
                textScore = ai.score;
                textReasoning = ai.reasoning;
            } catch (Exception e) {
                log.error("AI text-similarity failed for lost item {} vs found item {}, falling back to keyword overlap",
                        req.getLostItemId(), req.getFoundItemId(), e);
                textScore = computeTextSimilarity(req.getLostItemDescription(), req.getFoundItemDescription());
                textReasoning = "Based on shared keywords in the descriptions (AI comparison was unavailable).";
            }
        } else {
            textScore = computeTextSimilarity(req.getLostItemDescription(), req.getFoundItemDescription());
            textReasoning = haveBothDescriptions
                    ? "Based on shared keywords in the descriptions."
                    : "Not enough description text to compare.";
        }

        if (req.getLostCategory() != null && req.getLostCategory().equalsIgnoreCase(req.getFoundCategory())) {
            textScore = Math.min(1.0, textScore + 0.2);
        }

        double spatialScore = computeSpatialScore(req.getLostLocation(), req.getFoundLocation());

        double temporalScore = computeTemporalScore(req.getLostDate(), req.getFoundDate());

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
                .matchReasoning(textReasoning)
                .build();
    }

    private AiTextSimilarity computeTextSimilarityAi(String lostDescription, String foundDescription) throws Exception {
        String prompt = """
            You compare a lost-item report and a found-item report on a lost & found platform.
            Judge how likely they describe the SAME physical object, even if the wording,
            level of detail, or vocabulary differs (for example "insect" vs "animal",
            singular vs plural, or extra detail present on only one side).
            Respond with strict JSON only, no markdown: {"score": <number between 0.0 and 1.0>, "reasoning": "<one short sentence>"}.

            Lost item description: "%s"
            Found item description: "%s"
            """.formatted(lostDescription, foundDescription);

        String jsonResponse = callLlmApi(prompt);
        @SuppressWarnings("unchecked")
        Map<String, Object> parsed = objectMapper.readValue(jsonResponse, Map.class);

        Object rawScore = parsed.get("score");
        double score = rawScore instanceof Number ? ((Number) rawScore).doubleValue() : 0.0;
        score = Math.max(0.0, Math.min(1.0, score));

        Object rawReasoning = parsed.get("reasoning");
        String reasoning = rawReasoning != null ? rawReasoning.toString() : "AI comparison returned no reasoning.";

        return new AiTextSimilarity(score, reasoning);
    }

    private static final class AiTextSimilarity {
        final double score;
        final String reasoning;

        AiTextSimilarity(double score, String reasoning) {
            this.score = score;
            this.reasoning = reasoning;
        }
    }

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

    private double computeSpatialScore(String lostLocation, String foundLocation) {
        if (lostLocation == null || foundLocation == null
                || lostLocation.isBlank() || foundLocation.isBlank()) {
            return 0.5;
        }
        return lostLocation.trim().equalsIgnoreCase(foundLocation.trim()) ? 0.95 : 0.15;
    }

    private double computeTemporalScore(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) return 0.5;

        long hoursApart = Math.abs(Duration.between(d1, d2).toHours());
        final long maxScoreHours = 2 * 24;
        final long minScoreHours = 21 * 24;
        final double floorScore = 0.1;

        if (hoursApart <= maxScoreHours) return 1.0;
        if (hoursApart >= minScoreHours) return floorScore;

        double fraction = (double) (hoursApart - maxScoreHours) / (minScoreHours - maxScoreHours);
        return 1.0 - fraction * (1.0 - floorScore);
    }

    private String callLlmApi(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

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
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

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
