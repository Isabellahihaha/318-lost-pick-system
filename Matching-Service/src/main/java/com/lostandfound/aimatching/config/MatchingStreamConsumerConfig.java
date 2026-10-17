package com.lostandfound.aimatching.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lostandfound.aimatching.event.FoundItemReportedEvent;
import com.lostandfound.aimatching.event.LostItemReportedEvent;
import com.lostandfound.aimatching.service.MatchEventOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MatchingStreamConsumerConfig {

    private final MatchEventOrchestrator orchestrator;
    private final ObjectMapper objectMapper;

    @Bean
    public Consumer<byte[]> foundItemReported() {
        return payload -> {
            try {
                FoundItemReportedEvent event = objectMapper.readValue(payload, FoundItemReportedEvent.class);
                orchestrator.onFoundItemReported(event);
            } catch (Exception e) {
                log.error("Could not deserialize FoundItemReportedEvent off Kafka topic found-item-reported: {}",
                        e.getMessage(), e);
            }
        };
    }

    @Bean
    public Consumer<byte[]> lostItemReported() {
        return payload -> {
            try {
                LostItemReportedEvent event = objectMapper.readValue(payload, LostItemReportedEvent.class);
                orchestrator.onLostItemReported(event);
            } catch (Exception e) {
                log.error("Could not deserialize LostItemReportedEvent off Kafka topic lost-item-reported: {}",
                        e.getMessage(), e);
            }
        };
    }
}
