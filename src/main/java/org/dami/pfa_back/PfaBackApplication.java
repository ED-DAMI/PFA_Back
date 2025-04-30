package org.dami.pfa_back;

import org.dami.pfa_back.Documents.Song;
import org.dami.pfa_back.Repository.SongRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.*;

@SpringBootApplication
public class PfaBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(PfaBackApplication.class, args);
    }
    @Bean
    CommandLineRunner start(SongRepo songRepository) {
        return args -> {
            Song song = new Song();
            song.setId(UUID.randomUUID().toString());
            song.setTitle("Imagine");
            song.setArtist("John Lennon");
            song.setAlbum("Imagine");
            song.setGenre("Rock");
            song.setDuration(183);
            song.setReleaseDate(new Date());
            song.setLanguage("English");
            song.setUrlAudio("https://cdn.example.com/audio/imagine.mp3");
            song.setCoverImage("https://cdn.example.com/images/imagine.jpg");
            song.setTags(Arrays.asList("classic", "peace", "rock"));
            song.setLyrics("Imagine there's no heaven...");
            song.setCreatedAt(new Date());

            songRepository.save(song);

            System.out.println("🎵 Song inserted:");
            System.out.println(song);
        };
    }

}
