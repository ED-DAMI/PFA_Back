package org.dami.pfa_back;

import org.dami.pfa_back.Documents.history;
import org.dami.pfa_back.Repository.CommentReop;
import org.dami.pfa_back.Repository.SongRepo;
import org.dami.pfa_back.Repository.historyRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PfaBackApplication {
    private final SongRepo songRepo;

    public PfaBackApplication(SongRepo songRepo) {
        this.songRepo = songRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(PfaBackApplication.class, args);
    }


    CommandLineRunner start(SongRepo repo, historyRepo historyRepo , CommentReop commentReop) {
        return args -> {


        };

    }
}


