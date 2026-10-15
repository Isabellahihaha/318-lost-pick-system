package com.lostandfound.claimrecovery.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    /**
     * FR15: Real-time notifications for claim, matching, and recovery updates.
     */
    public void sendClaimStatusNotification(Long userId, Long claimId, String status, String message) {
        // In production, integrate with WebSockets (STOMP), FCM (Push), or Email/SMS dispatchers
        log.info("[NOTIFICATION SENT] To User ID: {} | Claim ID: {} | Status: {} | Message: {}", 
                userId, claimId, status, message);
    }

    public void notifyRecoveryCompleted(Long lostItemId, Long foundItemId) {
        log.info("[REAL-TIME EVENT] Recovery completed for Lost Item #{} and Found Item #{}", 
                lostItemId, foundItemId);
    }
}