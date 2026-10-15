package com.lostandfound.claimrecovery.controller;

import com.lostandfound.claimrecovery.dto.ClaimRequest;
import com.lostandfound.claimrecovery.dto.ClaimResponse;
import com.lostandfound.claimrecovery.dto.ClaimVerificationRequest;
import com.lostandfound.claimrecovery.service.ClaimRecoveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimRecoveryController {

    private final ClaimRecoveryService claimRecoveryService;

    // Initiate claim
    @PostMapping
    public ResponseEntity<ClaimResponse> createClaim(@Valid @RequestBody ClaimRequest request) {
        ClaimResponse response = claimRecoveryService.createClaim(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Verify ownership proof
    @PostMapping("/{claimId}/verify")
    public ResponseEntity<ClaimResponse> verifyClaim(
            @PathVariable Long claimId,
            @Valid @RequestBody ClaimVerificationRequest request) {
        ClaimResponse response = claimRecoveryService.verifyClaim(claimId, request);
        return ResponseEntity.ok(response);
    }

    // Mark as recovered
    @PostMapping("/{claimId}/complete-recovery")
    public ResponseEntity<ClaimResponse> completeRecovery(@PathVariable Long claimId) {
        ClaimResponse response = claimRecoveryService.completeRecovery(claimId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<ClaimResponse> getClaimById(@PathVariable Long claimId) {
        return ResponseEntity.ok(claimRecoveryService.getClaimById(claimId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(claimRecoveryService.getClaimsByUser(userId));
    }
}