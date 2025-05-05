package org.dami.pfa_back.DTO;

 // Adaptez le chemin de votre package DTO

//
import java.util.Date; // Ou LocalDateTime/LocalDate si vous préférez
import java.util.List;
import java.util.Objects; // Pour equals/hashCode

// @Data // Annotation Lombok pour générer getters, setters, toString, equals, hashCode
public class SongDto {

    private String id;
    private String title;
    private String artist; // Supposons que c'est le nom ou l'ID de l'artiste
    private String genre;  // Supposons que c'est le nom ou l'ID du genre
    private String album;
    private int duration;
    private Date releaseDate;
    private String language;
    private List<String> tags;
    private Date createdAt;



    // --- Constructeurs ---
    public SongDto() {

    }

    public SongDto(String id, String title, String artist, String genre, String album,
                       int duration, Date releaseDate, String language, List<String> tags, Date createdAt) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.album = album;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.language = language;
        this.tags = tags;
        this.createdAt = createdAt;
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getGenre() { return genre; }
    public String getAlbum() { return album; }
    public int getDuration() { return duration; }
    public Date getReleaseDate() { return releaseDate; }
    public String getLanguage() { return language; }
    public List<String> getTags() { return tags; }
    public Date getCreatedAt() { return createdAt; }

    // --- Setters ---
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setAlbum(String album) { this.album = album; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }
    public void setLanguage(String language) { this.language = language; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }


    // --- toString(), equals(), hashCode() ---
    @Override
    public String toString() {
        return "SongListDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                ", releaseDate=" + releaseDate +
                ", language='" + language + '\'' +
                ", tags=" + tags +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongDto that = (SongDto) o;
        return Objects.equals(id, that.id); // Comparaison basée sur l'ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash basé sur l'ID
    }
}
