package com.csci318.found.client;

public class MatchResponseDto {

    private Long lostItemId;
    private Long foundItemId;
    private Double totalConfidenceScore;
    private Double textSimilarityScore;
    private Double spatialProximityScore;
    private Double temporalProximityScore;
    private String matchStatus;
    private String matchReasoning;

    public Long getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(Long lostItemId) {
        this.lostItemId = lostItemId;
    }

    public Long getFoundItemId() {
        return foundItemId;
    }

    public void setFoundItemId(Long foundItemId) {
        this.foundItemId = foundItemId;
    }

    public Double getTotalConfidenceScore() {
        return totalConfidenceScore;
    }

    public void setTotalConfidenceScore(Double totalConfidenceScore) {
        this.totalConfidenceScore = totalConfidenceScore;
    }

    public Double getTextSimilarityScore() {
        return textSimilarityScore;
    }

    public void setTextSimilarityScore(Double textSimilarityScore) {
        this.textSimilarityScore = textSimilarityScore;
    }

    public Double getSpatialProximityScore() {
        return spatialProximityScore;
    }

    public void setSpatialProximityScore(Double spatialProximityScore) {
        this.spatialProximityScore = spatialProximityScore;
    }

    public Double getTemporalProximityScore() {
        return temporalProximityScore;
    }

    public void setTemporalProximityScore(Double temporalProximityScore) {
        this.temporalProximityScore = temporalProximityScore;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getMatchReasoning() {
        return matchReasoning;
    }

    public void setMatchReasoning(String matchReasoning) {
        this.matchReasoning = matchReasoning;
    }
}
