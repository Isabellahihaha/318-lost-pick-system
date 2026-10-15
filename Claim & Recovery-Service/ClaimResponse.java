package com.lostandfound.claimrecovery.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaimResponse {
    private Long claimId;
    private Long lostItemId;
    private Long foundItemId;
    private Long userId;
    private String proofDescription;
    private String status; // SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, RECOVERED
    private String reviewNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}