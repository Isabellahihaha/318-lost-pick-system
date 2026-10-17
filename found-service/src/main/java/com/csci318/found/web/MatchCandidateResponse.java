package com.csci318.found.web;

public class MatchCandidateResponse {

    private final Long lostItemId;
    private final String lostItemName;
    private final String lostItemDescription;
    private final String lostItemLocation;
    private final String lostItemEventDate;
    private final String lostItemContactEmail;
    private final String lostItemCategory;
    private final String lostItemOwnerUsername;
    private final double totalConfidenceScore;
    private final double textSimilarityScore;
    private final double spatialProximityScore;
    private final double temporalProximityScore;
    private final String matchStatus;
    private final String matchReasoning;

    public MatchCandidateResponse(Long lostItemId, String lostItemName, String lostItemDescription,
                                   String lostItemLocation, String lostItemEventDate, String lostItemContactEmail,
                                   String lostItemCategory, String lostItemOwnerUsername,
                                   double totalConfidenceScore, double textSimilarityScore,
                                   double spatialProximityScore, double temporalProximityScore, String matchStatus,
                                   String matchReasoning) {
        this.lostItemId = lostItemId;
        this.lostItemName = lostItemName;
        this.lostItemDescription = lostItemDescription;
        this.lostItemLocation = lostItemLocation;
        this.lostItemEventDate = lostItemEventDate;
        this.lostItemContactEmail = lostItemContactEmail;
        this.lostItemCategory = lostItemCategory;
        this.lostItemOwnerUsername = lostItemOwnerUsername;
        this.totalConfidenceScore = totalConfidenceScore;
        this.textSimilarityScore = textSimilarityScore;
        this.spatialProximityScore = spatialProximityScore;
        this.temporalProximityScore = temporalProximityScore;
        this.matchStatus = matchStatus;
        this.matchReasoning = matchReasoning;
    }

    public Long getLostItemId() {
        return lostItemId;
    }

    public String getLostItemName() {
        return lostItemName;
    }

    public String getLostItemDescription() {
        return lostItemDescription;
    }

    public String getLostItemLocation() {
        return lostItemLocation;
    }

    public String getLostItemEventDate() {
        return lostItemEventDate;
    }

    public String getLostItemContactEmail() {
        return lostItemContactEmail;
    }

    public String getLostItemCategory() {
        return lostItemCategory;
    }

    public String getLostItemOwnerUsername() {
        return lostItemOwnerUsername;
    }

    public double getTotalConfidenceScore() {
        return totalConfidenceScore;
    }

    public double getTextSimilarityScore() {
        return textSimilarityScore;
    }

    public double getSpatialProximityScore() {
        return spatialProximityScore;
    }

    public double getTemporalProximityScore() {
        return temporalProximityScore;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public String getMatchReasoning() {
        return matchReasoning;
    }
}
