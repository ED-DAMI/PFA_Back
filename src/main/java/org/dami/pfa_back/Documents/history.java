package org.dami.pfa_back.Documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "history")
public class history {
    @Id
    private String id;
    private String userId;
    private String songId;

    public history() {
    }

    public history(String id, String userId, String songId) {
        this.id = id;
        this.userId = userId;
        this.songId = songId;
    }

    public String getId() {
        return id;
    }

    public history setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public history setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getSongId() {
        return songId;
    }

    public history setSongId(String songId) {
        this.songId = songId;
        return this;
    }
}
