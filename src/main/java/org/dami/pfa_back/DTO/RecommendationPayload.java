package org.dami.pfa_back.DTO;

 // Ou un autre package approprié comme DTOs

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Pour ignorer les champs JSON non mappés
public class RecommendationPayload {
    private String userId;
    private List<String> songIds;

    // Constructeur par défaut (requis par Jackson pour la désérialisation)
    public RecommendationPayload() {
    }

    public RecommendationPayload(String userId, List<String> songIds) {
        this.userId = userId;
        this.songIds = songIds;
    }

    // Getters et Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }

    @Override
    public String toString() {
        return "RecommendationPayload{" +
                "userId='" + userId + '\'' +
                ", songIds=" + songIds +
                '}';
    }
}
