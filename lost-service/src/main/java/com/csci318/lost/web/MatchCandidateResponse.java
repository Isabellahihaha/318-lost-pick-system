package com.csci318.lost.web;

public class MatchCandidateResponse {

    private final Long foundItemId;
    private final String foundItemName;
    private final String foundItemDescription;
    private final String foundItemLocation;
    private final String foundItemEventDate;
    private final String foundItemContactEmail;
    private final String foundItemCategory;
    private final String foundItemOwnerUsername;
    private final double totalConfidenceScore;
    private final double textSimilarityScore;
    private final double spatialProximityScore;
    private final double temporalProximityScore;
    private final String matchStatus;
    private final String matchReasoning;

    public MatchCandidateResponse(Long foundItemId, String foundItemName, String foundItemDescription,
                                   String foundItemLocation, String foundItemEventDate, String foundItemContactEmail,
                                   String foundItemCategory, String foundItemOwnerUsername,
                                   double totalConfidenceScore, double textSimilarityScore,
                                   double spatialProximityScore, double temporalProximityScore, String matchStatus,
                                   String matchReasoning) {
        this.foundItemId = foundItemId;
        this.foundItemName = foundItemName;
        this.foundItemDescription = foundItemDescription;
        this.foundItemLocation = foundItemLocation;
        this.foundItemEventDate = foundItemEventDate;
        this.foundItemContactEmail = foundItemContactEmail;
        this.foundItemCategory = foundItemCategory;
        this.foundItemOwnerUsername = foundItemOwnerUsername;
        this.totalConfidenceScore = totalConfidenceScore;
        this.textSimilarityScore = textSimilarityScore;
        this.spatialProximityScore = spatialProximityScore;
        this.temporalProximityScore = temporalProximityScore;
        this.matchStatus = matchStatus;
        this.matchReasoning = matchReasoning;
    }

    public Long getFoundItemId() {
        return foundItemId;
    }

    public String getFoundItemName() {
        return foundItemName;
    }

    public String getFoundItemDescription() {
        return foundItemDescription;
    }

    public String getFoundItemLocation() {
        return foundItemLocation;
    }

    public String getFoundItemEventDate() {
        return foundItemEventDate;
    }

    public String getFoundItemContactEmail() {
        return foundItemContactEmail;
    }

    public String getFoundItemCategory() {
        return foundItemCategory;
    }

    public String getFoundItemOwnerUsername() {
        return foundItemOwnerUsername;
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
