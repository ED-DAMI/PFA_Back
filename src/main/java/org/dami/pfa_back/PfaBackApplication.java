package org.dami.pfa_back;

import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Documents.Song; // Importer la classe Song correcte
import org.dami.pfa_back.Repository.PlaylistRepo;
import org.dami.pfa_back.Repository.SongRepo;
import org.dami.pfa_back.Services.SongService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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

    CommandLineRunner start(SongRepo repo){
        return args ->{
            repo.deleteAll();
            Song song1 = new Song();
            song1.setTitle("Shape of You");
            song1.setArtist("Ed Sheeran");
            song1.setGenre("Pop");
            song1.setAlbum("Divide");
            song1.setDuration(100);
            song1.setAudioFileExtension(".mpeg");
            song1.setCreatedAt(new Date());

            Song song2 = new Song();
            song2.setTitle("Blinding Lights");
            song2.setArtist("The Weeknd");
            song2.setGenre("Synthpop");
            song2.setDuration(130);
            song2.setAlbum("After Hours");
            song2.setAudioFileExtension(".mpeg");
            song2.setCreatedAt(new Date());

            Song song3 = new Song();
            song3.setTitle("Bohemian Rhapsody");
            song3.setArtist("Queen");
            song3.setGenre("Rock");
            song3.setDuration(120);
            song3.setAlbum("A Night at the Opera");
            song3.setAudioFileExtension(".mpeg");
            song3.setCreatedAt(new Date());
            Stream.of(song3,song2,song3).forEach(repo::save);



        };
  }

    private String IP() {
        var IP="";
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            IP=localHost.getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("Impossible de récupérer l'adresse IP locale.");
    }
        return IP;
}
}
