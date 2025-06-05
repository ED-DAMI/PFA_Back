package org.dami.pfa_back.Documents;

import org.dami.pfa_back.Documents.Enums.Emoji;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field; // Peut être utile pour typer les champs Date
import org.springframework.data.elasticsearch.annotations.FieldType; // Peut être utile pour typer les champs Date


import java.util.ArrayList;
import java.util.Date; // Gardons java.util.Date comme dans votre code original
import java.util.List;
import java.util.Objects; // Pour equals/hashCode si besoin

@Document(indexName = "songs")

public class Song {
    @Id
    private String id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int duration;
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'||strict_date_optional_time||epoch_millis")
    private Date releaseDate;
    private String language;
    private List<String> tags;
    private String lyrics;
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'||strict_date_optional_time||epoch_millis")
    private Date createdAt;
    private String audioFileExtension;
    private String coverImageFileExtension;
    private int commentCount;
    private long totalReactionCount;
    private long ViewCount;

    public int getCommentCount() {
        return commentCount;
    }
    public Song setCommentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public long getTotalReactionCount() {
        return totalReactionCount;
    }

    public Song setTotalReactionCount(long totalReactionCount) {
        this.totalReactionCount = totalReactionCount;
        return this;
    }




    public long getViewCount() {
        return ViewCount;
    }

    public Song setViewCount(long viewCount) {
        ViewCount = viewCount;
        return this;
    }




    public Song() {

    }

    // Constructeur avec tous les arguments (y compris les nouveaux)
    public Song(String id, String title, String artist, String album, String genre, int duration,
                Date releaseDate, String language, List<String> tags, String lyrics, Date createdAt,
                String audioFileExtension, String coverImageFileExtension) { // Ajout des nouveaux params
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;

        this.duration = duration;

        this.releaseDate = releaseDate;
        this.language = language;
        this.tags = tags;
        this.lyrics = lyrics;
        this.createdAt = createdAt;
        this.audioFileExtension = audioFileExtension;
        this.coverImageFileExtension = coverImageFileExtension;
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

    public String getAudioFileExtension() {
        return audioFileExtension;
    }

    public Song setAudioFileExtension(String audioFileExtension) {
        this.audioFileExtension = audioFileExtension;
        return this;
    }

    public String getCoverImageFileExtension() {
        return coverImageFileExtension;
    }

    public Song setCoverImageFileExtension(String coverImageFileExtension) {
        this.coverImageFileExtension = coverImageFileExtension;
        return this;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id); // Comparer uniquement sur l'ID
    }

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
                ", tags=" + tags +
                ", lyrics='" + lyrics + '\'' +
                ", createdAt=" + createdAt +
                ", audioFileExtension='" + audioFileExtension + '\'' +
                ", coverImageFileExtension='" + coverImageFileExtension + '\'' +
                ", commentCount=" + commentCount +
                ", totalReactionCount=" + totalReactionCount +
                ", ViewCount=" + ViewCount +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hasher uniquement sur l'ID
    }

    public void IncrementView() {
        this.ViewCount++;
    }
}
