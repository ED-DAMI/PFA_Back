package org.dami.pfa_back.Documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;


@Document(indexName = "playlists")
public class Playlist {
    public Playlist(String id, String name, String description, List<String> songs, String ownerId, boolean isPublic, Date createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songs = songs;
        this.userID = ownerId;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }

    public Playlist() {
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", songs=" + songs +
                ", ownerId='" + userID + '\'' +
                ", isPublic=" + isPublic +
                ", createdAt=" + createdAt +
                '}';
    }

    public String getId() {
        return id;
    }

    public Playlist setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Playlist setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Playlist setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getSongs() {
        return songs;
    }

    public Playlist setSongs(List<String> songs) {
        this.songs = songs;
        return this;
    }

    public String getuserID() {
        return userID;
    }

    public Playlist setuserID(String ownerId) {
        this.userID = ownerId;
        return this;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Playlist setPublic(boolean aPublic) {
        isPublic = aPublic;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Playlist setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Id
    private String id;
    private String name;
    private String description;
    private List<String> songs;
    private String userID;
    private boolean isPublic;
    private Date createdAt;
}

