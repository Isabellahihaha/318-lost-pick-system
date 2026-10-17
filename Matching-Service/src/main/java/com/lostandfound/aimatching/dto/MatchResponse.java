package com.lostandfound.aimatching.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResponse {
    private Long lostItemId;
    private Long foundItemId;
    private Double totalConfidenceScore;
    private Double textSimilarityScore;
    private Double spatialProximityScore;
    private Double temporalProximityScore;
    private String matchStatus;
    private String matchReasoning;
}
