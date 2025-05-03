package org.dami.pfa_back.DTO;

import org.dami.pfa_back.Documents.Playlist;

public class PlaylistDTO {
    private String ID;
    private String name;
    private String  description;
    private int songCount;
    public String getID() {
        return ID;
    }

    public PlaylistDTO setID(String ID) {
        this.ID = ID;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaylistDTO setName(String name) {
        this.name = name;
        return this;
    }

    public int getSongCount() {
        return songCount;
    }

    public PlaylistDTO setSongCount(int songCount) {
        this.songCount = songCount;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PlaylistDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public PlaylistDTO() {
    }

    public PlaylistDTO(String ID, String name, int songCount) {
        this.ID = ID;
        this.name = name;
        this.songCount = songCount;
    }

    public static PlaylistDTO mapPlylist(Playlist playlist) {
    return new PlaylistDTO(playlist.getId(),playlist.getName(),playlist.getSongs().size()).setDescription(playlist.getDescription());

    }
}
