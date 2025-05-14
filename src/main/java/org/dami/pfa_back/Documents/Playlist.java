package org.dami.pfa_back.Documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Document(indexName = "playlists")
public class Playlist {
    @Id
    private String id;
    private String name;
    private String ownerId; // ID de l'utilisateur propriétaire
    private List<String> songIds=new ArrayList<>(); // Liste des IDs des chansons dans la playlist
    private String description;
    private String coverImageUrl;
    private Date createdAt;
    private Date updatedAt;

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", songIds=" + songIds +
                ", description='" + description + '\'' +
                ", coverImageUrl='" + coverImageUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
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

    public String getOwnerId() {
        return ownerId;
    }

    public Playlist setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public Playlist setSongIds(List<String> songIds) {
        this.songIds = songIds;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Playlist setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public Playlist setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Playlist setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Playlist setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Playlist() {
    }

    public Playlist(String id, String name, String ownerId, List<String> songIds, String description, String coverImageUrl, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.songIds = songIds;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
