package org.dami.pfa_back;

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

@SpringBootApplication
public class PfaBackApplication {
    private final SongRepo songRepo;

    public PfaBackApplication(SongRepo songRepo) {
        this.songRepo = songRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(PfaBackApplication.class, args);
    }

    CommandLineRunner start(SongRepo repo, CommentReop commentReop){
        return args ->{
            for (Song song : repo.findAll()) {
                int size = commentReop.findBySongId(song.getId()).size();
                repo.save(song.setCommentCount(size));
            }
        };
  }
}
