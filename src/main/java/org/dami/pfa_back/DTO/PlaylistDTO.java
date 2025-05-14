package org.dami.pfa_back.DTO;

public class PlaylistDTO {
    private String name;
    private boolean isPublic;
    private String description;

    public PlaylistDTO() {
    }

    public PlaylistDTO(String name, boolean isPublic, String description) {
        this.name = name;
        this.isPublic = isPublic;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public PlaylistDTO setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public PlaylistDTO setPublic(boolean aPublic) {
        isPublic = aPublic;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PlaylistDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
