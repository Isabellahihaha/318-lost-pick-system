package com.csci318.lost.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class FoundServiceClient {

    private static final Logger log = LoggerFactory.getLogger(FoundServiceClient.class);

    private final RestTemplate restTemplate;
    private final String foundServiceUrl;

    public FoundServiceClient(RestTemplate restTemplate,
                               @Value("${found.service.url:http://localhost:8082}") String foundServiceUrl) {
        this.restTemplate = restTemplate;
        this.foundServiceUrl = foundServiceUrl;
    }

    public List<FoundItemView> listUnresolved() {
        String url = foundServiceUrl + "/api/found-items";
        try {
            FoundItemView[] items = restTemplate.getForObject(url, FoundItemView[].class);
            if (items == null) {
                return List.of();
            }
            return Arrays.stream(items).filter(item -> !item.isResolved()).toList();
        } catch (RestClientException e) {
            log.warn("found-service unavailable while fetching match candidates: {}", e.getMessage());
            return List.of();
        }
    }
}
