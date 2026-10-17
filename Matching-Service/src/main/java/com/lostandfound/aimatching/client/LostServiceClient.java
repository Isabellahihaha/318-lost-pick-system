package com.lostandfound.aimatching.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class LostServiceClient {

    private final RestTemplate restTemplate;
    private final String lostServiceUrl;

    public LostServiceClient(RestTemplate restTemplate,
                              @Value("${lost.service.url:http://localhost:8081}") String lostServiceUrl) {
        this.restTemplate = restTemplate;
        this.lostServiceUrl = lostServiceUrl;
    }

    public List<LostItemView> listUnresolved() {
        String url = lostServiceUrl + "/api/lost-items";
        try {
            LostItemView[] items = restTemplate.getForObject(url, LostItemView[].class);
            if (items == null) {
                return List.of();
            }
            return Arrays.stream(items).filter(item -> !item.isResolved()).toList();
        } catch (RestClientException e) {
            log.warn("lost-service unavailable while fetching async match candidates: {}", e.getMessage());
            return List.of();
        }
    }
}
