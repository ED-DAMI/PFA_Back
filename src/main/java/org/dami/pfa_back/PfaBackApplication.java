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
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SpringBootApplication
public class PfaBackApplication {
    private final SongRepo songRepo;

    public PfaBackApplication(SongRepo songRepo) {
        this.songRepo = songRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(PfaBackApplication.class, args);
    }


    CommandLineRunner start(SongRepo repo, ReactionRepo reactionRepo, CommentReop commentReop) {
        return args -> {
            List<Song> songs = new ArrayList<>();
            Random random = new Random();

            String[] titles = {
                    "Espresso", "Texas Hold 'Em", "Birds of a Feather", "Too Sweet",
                    "Die With a Smile", "Good Luck, Babe!", "Not Like Us", "So American",
                    "Piece of My Heart", "Taste", "Padam Padam", "Water"
            };

            String[] artists = {
                    "Sabrina Carpenter", "Beyoncé", "Billie Eilish", "Hozier",
                    "Bruno Mars & Lady Gaga", "Chappell Roan", "Kendrick Lamar", "Olivia Rodrigo",
                    "Wizkid ft. Brent Faiyaz", "Sabrina Carpenter", "Kylie Minogue", "Tyla"
            };

            String[] albums = {
                    "Short n' Sweet", "Cowboy Carter", "Hit Me Hard and Soft", "Unreal Unearth",
                    "Single", "The Rise and Fall of a Midwest Princess", "Not Like Us - Single", "Guts (Spilled)",
                    "Morayo", "Short n' Sweet", "Tension", "Tyla"
            };

            String[] genres = {"Pop", "R&B", "Soul", "Afropop", "Synthpop", "Country"};
            String[] languages = {"English"};
            String[] tagPool = {"hit", "2024", "trending", "viral", "award-winning"};

            for (int i = 13; i <= 24; i++) {
                String id = "music_" + i;
                String title = titles[i - 13];
                String artist = artists[i - 13];
                String album = albums[i - 13];
                String genre = genres[random.nextInt(genres.length)];
                String tag = tagPool[random.nextInt(tagPool.length)];
                int duration = 180 + random.nextInt(60); // Durée entre 180 et 240 secondes

                Date releaseDate = new Date(System.currentTimeMillis() - random.nextInt(1_000_000_000));
                String language = languages[0];
                List<String> tags = Arrays.asList(tag, "popular", "2024");
                String lyrics = "Lyrics not available.";
                Date createdAt = new Date();
                String audioExt = ".mpeg";
                String coverExt = ".jpeg";

                Song song = new Song(
                        id, title, artist, album, genre, tag, duration,
                        releaseDate, language, tags, lyrics, createdAt, audioExt, coverExt
                );

                song.setViewCount(random.nextInt(10000)); // Nombre de vues aléatoire

                songs.add(song);
            }


            songRepo.saveAll(songs);

        };

    }
}


