package org.dami.pfa_back.Documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

@Document(indexName = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;

    public User() {

    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    private String role; // LISTENER, ARTIST, ADMIN
    private List<String> favorites;
    private List<String> playlists;
    private Date createdAt;

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRole() {
        return role;
    }

    public User setRole(String role) {
        this.role = role;
        return this;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public User setFavorites(List<String> favorites) {
        this.favorites = favorites;
        return this;
    }

    public List<String> getPlaylists() {
        return playlists;
    }

    public User setPlaylists(List<String> playlists) {
        this.playlists = playlists;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public User(String id, String username, String email, String role, List<String> favorites, List<String> playlists, Date createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.favorites = favorites;
        this.playlists = playlists;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", favorites=" + favorites +
                ", playlists=" + playlists +
                ", createdAt=" + createdAt +
                '}';
    }
}

