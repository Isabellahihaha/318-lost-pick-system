package com.csci318.found.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class LostServiceClient {

    private static final Logger log = LoggerFactory.getLogger(LostServiceClient.class);

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
            log.warn("lost-service unavailable while fetching match candidates: {}", e.getMessage());
            return List.of();
        }
    }
}
