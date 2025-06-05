package org.dami.pfa_back.DTO;

import java.util.List;

public class SongRecommendation {
    private String userId;
    private List<String> rcommandation;


    @Override
    public String toString() {
        return "SongRecommendation{" +
                "userId='" + userId + '\'' +
                ", rrcommandation=" + rcommandation +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public SongRecommendation setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public List<String> getRcommandation() {
        return rcommandation;
    }

    public SongRecommendation setRcommandation(List<String> rcommandation) {
        this.rcommandation = rcommandation;
        return this;
    }
}
