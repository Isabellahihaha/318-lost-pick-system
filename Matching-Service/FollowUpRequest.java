package com.lostandfound.aimatching.dto;

import lombok.Data;

@Data
public class FollowUpRequest {
    private ExtractedItemAttributes currentAttributes;
    private String originalDescription;
    private String reportType; // "LOST" or "FOUND"
}