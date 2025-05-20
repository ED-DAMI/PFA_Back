package org.dami.pfa_back.Services;

import org.dami.pfa_back.DTO.RecommendationRequest;
import org.dami.pfa_back.DTO.SongDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RecommendationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonApiUrl = "http://localhost:8000/api/recommendations/hybrid";

    public List<SongDto> getHybridRecommendations(String userId, int limit) {
        // Build the request body
        RecommendationRequest request = new RecommendationRequest();
        request.setUserId(userId);
        request.setLimit(limit);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the HTTP entity
        HttpEntity<RecommendationRequest> entity = new HttpEntity<>(request, headers);

        // Make the POST request
        ResponseEntity<List<SongDto>> response = restTemplate.exchange(
                pythonApiUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<List<SongDto>>() {}
        );

        return response.getBody();
    }
}