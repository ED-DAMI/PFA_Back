package org.dami.pfa_back;

import org.apache.http.conn.util.PublicSuffixListParser;
import org.dami.pfa_back.Documents.Song; // Importer la classe Song correcte
import org.dami.pfa_back.Repository.CommentReop;
import org.dami.pfa_back.Repository.ReactionRepo;
import org.dami.pfa_back.Repository.SongRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootApplication
public class PfaBackApplication {
    private final SongRepo songRepo;

    public PfaBackApplication(SongRepo songRepo) {
        this.songRepo = songRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(PfaBackApplication.class, args);
    }
     @Bean
    CommandLineRunner start(SongRepo repo,ReactionRepo reactionRepo){
        return args ->{
            repo.deleteAll();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Song song1 = new Song()
                    .setId("wc7XoZYB0O3F18ReDNho")
                    .setTitle("Blinding Lights")
                    .setArtist("The Weeknd")
                    .setAlbum("After Hours")
                    .setGenre("Synthpop")
                    .setDuration(130)
                    .setCommentCount(2)
                    .setTotalReactionCount(26)
                    .setViewCount(20)
                    .setCreatedAt(sdf.parse("2025-05-05T19:05:46.686Z"))
                    .setAudioFileExtension(".mpeg")
                    .setCoverImageFileExtension(".jpeg");

            Song song2 = new Song()
                    .setId("song-001")
                    .setTitle("Ocean Breeze")
                    .setArtist("Artist A")
                    .setAlbum("Summer Vibes")
                    .setGenre("Chillwave")
                    .setDuration(210)
                    .setCommentCount(5)
                    .setTotalReactionCount(33)
                    .setViewCount(23)
                    .setReleaseDate(sdf.parse("2025-01-14T23:00:00.000Z"))
                    .setLanguage("English")
                    .setTags(Arrays.asList("relaxing", "summer", "electronic"))
                    .setLyrics("[Instrumental]")
                    .setCreatedAt(sdf.parse("2025-05-06T08:48:39.675Z"))
                    .setAudioFileExtension(".mp3")
                    .setCoverImageFileExtension(".jpeg");

            Song song3 = new Song()
                    .setId("song-002")
                    .setTitle("Mountain Echoes")
                    .setArtist("Artist B")
                    .setAlbum("Nature Sounds")
                    .setGenre("Ambient")
                    .setDuration(180)
                    .setCommentCount(2)
                    .setTotalReactionCount(29)
                    .setViewCount(29)
                    .setReleaseDate(sdf.parse("2025-02-09T23:00:00.000Z"))
                    .setLanguage("Instrumental")
                    .setTags(Arrays.asList("nature", "ambient", "meditation"))
                    .setLyrics("")
                    .setCreatedAt(sdf.parse("2025-05-06T08:48:39.675Z"))
                    .setAudioFileExtension(".mpeg")
                    .setCoverImageFileExtension(".jpeg");

            Song song4 = new Song()
                    .setId("wM7XoZYB0O3F18ReCdjW")
                    .setTitle("Bohemian Rhapsody")
                    .setArtist("Queen")
                    .setAlbum("A Night at the Opera")
                    .setGenre("Rock")
                    .setDuration(120)
                    .setCommentCount(3)
                    .setTotalReactionCount(24)
                    .setViewCount(135)
                    .setCreatedAt(sdf.parse("2025-05-05T19:05:46.686Z"))
                    .setAudioFileExtension(".mpeg")
                    .setCoverImageFileExtension(".jpeg");

            Stream
                    .of(song1,song4,song3,song2)
                    .forEach(songRepo::save);
        };
  }
}
