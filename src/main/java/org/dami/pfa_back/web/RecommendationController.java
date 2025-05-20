package org.dami.pfa_back.web;

import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.Services.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations/hybrid")
    public List<SongDto> getHybridRecommendations(
            @RequestParam String userId,
            @RequestParam(defaultValue = "10") int limit) {
        return recommendationService.getHybridRecommendations(userId, limit);
    }
}
