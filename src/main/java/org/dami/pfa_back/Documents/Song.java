package org.dami.pfa_back.Documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;
@Document(indexName = "songs")
public class Song {
    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", releaseDate=" + releaseDate +
                ", language='" + language + '\'' +
                ", urlAudio='" + urlAudio + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", tags=" + tags +
                ", lyrics='" + lyrics + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Id
    private String id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int duration;
    private Date releaseDate;
    private String language;
    private String urlAudio;
    private String coverImage;
    private List<String> tags;
    private String lyrics;
    private Date createdAt;

    public Song(String id, String title, String artist, String album, String genre, int duration, Date releaseDate, String language, String urlAudio, String coverImage, List<String> tags, String lyrics, Date createdAt) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.language = language;
        this.urlAudio = urlAudio;
        this.coverImage = coverImage;
        this.tags = tags;
        this.lyrics = lyrics;
        this.createdAt = createdAt;
    }

    public Song() {
    }



    public String getId() {
        return id;
    }

    public Song setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Song setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Song setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Song setAlbum(String album) {
        this.album = album;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public Song setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Song setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Song setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public Song setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getUrlAudio() {
        return urlAudio;
    }

    public Song setUrlAudio(String urlAudio) {
        this.urlAudio = urlAudio;
        return this;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public Song setCoverImage(String coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public Song setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public String getLyrics() {
        return lyrics;
    }

    public Song setLyrics(String lyrics) {
        this.lyrics = lyrics;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Song setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
