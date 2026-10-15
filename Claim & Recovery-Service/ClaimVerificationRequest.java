package com.lostandfound.claimrecovery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClaimVerificationRequest {
    @NotNull(message = "Verifier User ID is required")
    private Long verifierId;

    @NotNull(message = "Approval status is required")
    private Boolean approved;

    private String reviewNotes;
}