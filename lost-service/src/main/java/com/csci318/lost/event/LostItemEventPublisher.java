package com.csci318.lost.event;

import com.csci318.lost.domain.LostItemRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class LostItemEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LostItemEventPublisher.class);

    private static final String BINDING_NAME = "lostItemReported-out-0";

    private final StreamBridge streamBridge;

    public LostItemEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishLostItemReported(LostItemRequest item) {
        LostItemReportedEvent event = new LostItemReportedEvent(
                UUID.randomUUID().toString(),
                item.getId(),
                item.getName(),
                item.getCategory(),
                item.getDescription(),
                item.getLocation(),
                item.getEventDate(),
                item.getContactEmail(),
                item.getOwnerUsername(),
                Instant.now()
        );
        try {
            boolean sent = streamBridge.send(BINDING_NAME, event);
            if (!sent) {
                log.warn("LostItemReported event for lost item {} was not accepted by the Kafka binder", item.getId());
            }
        } catch (Exception e) {
            log.warn("Could not publish LostItemReported event for lost item {} (Kafka unreachable?): {}",
                    item.getId(), e.getMessage());
        }
    }
}
