package com.csci318.found.event;

import com.csci318.found.domain.FoundItemPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class FoundItemEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(FoundItemEventPublisher.class);

    private static final String BINDING_NAME = "foundItemReported-out-0";

    private final StreamBridge streamBridge;

    public FoundItemEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishFoundItemReported(FoundItemPost item) {
        FoundItemReportedEvent event = new FoundItemReportedEvent(
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
                log.warn("FoundItemReported event for found item {} was not accepted by the Kafka binder", item.getId());
            }
        } catch (Exception e) {
            log.warn("Could not publish FoundItemReported event for found item {} (Kafka unreachable?): {}",
                    item.getId(), e.getMessage());
        }
    }
}
