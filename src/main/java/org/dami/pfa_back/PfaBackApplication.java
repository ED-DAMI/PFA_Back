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
import java.text.SimpleDateFormat;
import java.util.*;
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
    @Bean
    CommandLineRunner start(SongRepo repo){
        return args ->{
            for (Song song : repo.findAll()) {
                song.setCoverImageFileExtension(".jpeg");
                repo.save(song);
            }
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
