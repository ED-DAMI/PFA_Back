package org.dami.pfa_back.DTO;

public class EmojiRequest {
    private String emoji; // Field name must match the JSON key "emoji"

    // Getters and Setters are crucial for Jackson deserialization
    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
