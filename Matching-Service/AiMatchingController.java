package com.lostandfound.aimatching.controller;

import com.lostandfound.aimatching.dto.*;
import com.lostandfound.aimatching.service.AiMatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-matching")
@RequiredArgsConstructor
public class AiMatchingController {

    private final AiMatchingService aiMatchingService;

    @PostMapping("/extract-attributes")
    public ResponseEntity<ExtractedItemAttributes> extractAttributes(@Valid @RequestBody ExtractionRequest request) {
        ExtractedItemAttributes attributes = aiMatchingService.extractAttributes(request.getDescription());
        return ResponseEntity.ok(attributes);
    }

    @PostMapping("/generate-followups")
    public ResponseEntity<FollowUpResponse> generateFollowups(@RequestBody FollowUpRequest request) {
        FollowUpResponse response = aiMatchingService.generateFollowUps(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate-match")
    public ResponseEntity<MatchResponse> calculateMatch(@Valid @RequestBody MatchCalculationRequest request) {
        MatchResponse matchResponse = aiMatchingService.calculateMatchScore(request);
        return ResponseEntity.ok(matchResponse);
    }
}