package org.dami.pfa_back.DTO;

// org.dami.pfa_back.DTO.UserDto.java


// import lombok.Data; // Si vous utilisez Lombok
// @Data
public class UserDto {
    private String id;
    private String username;
    private String email; // Optionnel, selon ce que vous voulez exposer
    private String avatarUrl; // URL complète vers l'avatar

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
