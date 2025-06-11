package org.dami.pfa_back.DTO;

// Dans un nouveau fichier PlaylistNameUpdateDTO.java dans votre package DTO

public class PlaylistNameUpdateDTO {
    private String name;

    // Constructeur vide requis pour la désérialisation JSON
    public PlaylistNameUpdateDTO() {
    }

    public PlaylistNameUpdateDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
