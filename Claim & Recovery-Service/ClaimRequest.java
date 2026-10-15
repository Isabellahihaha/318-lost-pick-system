package com.lostandfound.claimrecovery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClaimRequest {
    @NotNull(message = "Lost Item ID is required")
    private Long lostItemId;

    @NotNull(message = "Found Item ID is required")
    private Long foundItemId;

    @NotNull(message = "Claimant User ID is required")
    private Long userId;

    @NotBlank(message = "Proof description is required")
    private String proofDescription;

    private String receiptImageUrl;
    private String serialNumberProvided;
}