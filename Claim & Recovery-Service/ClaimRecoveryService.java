package com.lostandfound.claimrecovery.service;

import com.lostandfound.claimrecovery.client.FoundItemClient;
import com.lostandfound.claimrecovery.client.LostItemClient;
import com.lostandfound.claimrecovery.dto.ClaimResponse;
import com.lostandfound.claimrecovery.model.Claim;
import com.lostandfound.claimrecovery.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClaimRecoveryService {

    private final ClaimRepository claimRepository;
    private final NotificationService notificationService;
    private final LostItemClient lostItemClient;   // Feign or RestTemplate client
    private final FoundItemClient foundItemClient; // Feign or RestTemplate client

    /**
     * Record final item recovery status & hide items from public feeds
     */
    @Transactional
    public ClaimResponse completeRecovery(Long claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Claim not found with ID: " + claimId));

        if (claim.getStatus() != Claim.ClaimStatus.APPROVED) {
            throw new IllegalStateException("Only APPROVED claims can be marked as RECOVERED.");
        }

        // 1. Update claim status to RECOVERED
        claim.setStatus(Claim.ClaimStatus.RECOVERED);
        Claim completedClaim = claimRepository.save(claim);

        // 2. Hide items by updating status in Lost Service and Found Service
        try {
            lostItemClient.updateItemStatus(completedClaim.getLostItemId(), "RESOLVED");
            foundItemClient.updateItemStatus(completedClaim.getFoundItemId(), "RECOVERED");
            log.info("Successfully updated Lost Item #{} and Found Item #{} to non-public statuses.",
                    completedClaim.getLostItemId(), completedClaim.getFoundItemId());
        } catch (Exception e) {
            log.error("Failed to synchronize recovery status with Lost/Found services", e);
            // Optionally retry or publish to a dead-letter queue / transactional outbox
        }

        // 3. Trigger real-time notifications
        notificationService.notifyRecoveryCompleted(completedClaim.getLostItemId(), completedClaim.getFoundItemId());
        notificationService.sendClaimStatusNotification(
                completedClaim.getUserId(),
                completedClaim.getId(),
                completedClaim.getStatus().name(),
                "Item recovery complete! The item listing has been archived."
        );

        return mapToResponse(completedClaim);
    }

    private ClaimResponse mapToResponse(Claim claim) {
        ClaimResponse response = new ClaimResponse();
        response.setClaimId(claim.getId());
        response.setLostItemId(claim.getLostItemId());
        response.setFoundItemId(claim.getFoundItemId());
        response.setUserId(claim.getUserId());
        response.setProofDescription(claim.getProofDescription());
        response.setStatus(claim.getStatus().name());
        response.setReviewNotes(claim.getReviewNotes());
        response.setCreatedAt(claim.getCreatedAt());
        response.setUpdatedAt(claim.getUpdatedAt());
        return response;
    }
}