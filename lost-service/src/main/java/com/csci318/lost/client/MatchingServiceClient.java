package com.csci318.lost.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class MatchingServiceClient {

    private static final Logger log = LoggerFactory.getLogger(MatchingServiceClient.class);

    private final RestTemplate restTemplate;
    private final String matchingServiceUrl;

    public MatchingServiceClient(RestTemplate restTemplate,
                                  @Value("${matching.service.url:http://localhost:8085}") String matchingServiceUrl) {
        this.restTemplate = restTemplate;
        this.matchingServiceUrl = matchingServiceUrl;
    }

    public MatchResponseDto calculateMatch(MatchCalculationRequestDto request) {
        String url = matchingServiceUrl + "/api/ai-matching/calculate-match";
        try {
            return restTemplate.postForObject(url, request, MatchResponseDto.class);
        } catch (RestClientException e) {
            log.warn("Matching-Service unavailable or errored while scoring lost item {} vs found item {}: {}",
                    request.getLostItemId(), request.getFoundItemId(), e.getMessage());
            return null;
        }
    }
}
