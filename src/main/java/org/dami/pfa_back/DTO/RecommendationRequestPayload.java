package org.dami.pfa_back.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendationRequestPayload { // Nouveau nom pour clarté
    private String userId;
    private List<String> songIds;

    // Constructeur par défaut
    public RecommendationRequestPayload() {}

    public RecommendationRequestPayload(String userId, List<String> songIds) {
        this.userId = userId;
        this.songIds = songIds;
    }

    // Getters et Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<String> getSongIds() { return songIds; }
    public void setSongIds(List<String> songIds) { this.songIds = songIds; }

    @Override
    public String toString() {
        return "RecommendationRequestPayload{" +
                "userId='" + userId + '\'' +
                ", songIds=" + songIds +
                '}';
    }
}
