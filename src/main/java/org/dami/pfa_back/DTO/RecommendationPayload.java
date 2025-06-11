package org.dami.pfa_back.DTO;

 // Ou un autre package approprié comme DTOs

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Pour ignorer les champs JSON non mappés
public class RecommendationPayload {
    private String userId;
    private List<SongDto> songs;

    // Constructeur par défaut (requis par Jackson pour la désérialisation)
    public RecommendationPayload() {
    }

    public String getUserId() {
        return userId;
    }

    public RecommendationPayload setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public RecommendationPayload(String userId, List<SongDto> songs) {
        this.userId = userId;
        this.songs = songs;
    }

    public List<SongDto> getSongs() {
        return songs;
    }

    public RecommendationPayload setSongs(List<SongDto> songs) {
        this.songs = songs;
        return this;
    }
}

