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
public class FoundServiceClient {

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
            log.warn("found-service unavailable while fetching async match candidates: {}", e.getMessage());
            return List.of();
        }
    }
}
