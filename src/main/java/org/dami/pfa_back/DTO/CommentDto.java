package org.dami.pfa_back.DTO;

public class CommentDto {
    private String text;
    public String getText() {
        return text;
    }
    public CommentDto setText(String text) {
        this.text = text;
        return this;
    }
}
