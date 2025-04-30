package org.dami.pfa_back.Documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "comments")
public class Comment {
    @Id
    private String id;
    private String userId;
    private String songId;
    private String text;
    private int rating; // 1 à 5
    private Date createdAt;

    public Comment(String id, String userId, String songId, String text, int rating, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.songId = songId;
        this.text = text;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public Comment setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Comment setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getSongId() {
        return songId;
    }

    public Comment setSongId(String songId) {
        this.songId = songId;
        return this;
    }

    public String getText() {
        return text;
    }

    public Comment setText(String text) {
        this.text = text;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public Comment setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Comment setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}

