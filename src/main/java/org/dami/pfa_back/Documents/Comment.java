package org.dami.pfa_back.Documents;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "comments")
public class Comment {
    @Id
    private String id;
    private String author;
    private String songId;
    private String text;
    private Date createdAt;
    public Comment() {
    }
    public String getId() {
        return id;
    }
    public Comment setId(String id) {
        this.id = id;
        return this;
    }
    public Comment(String id, String author, String songId, String text, Date createdAt) {
        this.id = id;
        this.author = author;
        this.songId = songId;
        this.text = text;
        this.createdAt = createdAt;
    }
    public String getAuthor() {
        return author;
    }
    public Comment setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getSongId() {return songId;}

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
    public Date getCreatedAt() {
        return createdAt;
    }
    public Comment setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}

