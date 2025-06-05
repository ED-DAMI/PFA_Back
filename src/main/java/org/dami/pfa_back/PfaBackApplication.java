package org.dami.pfa_back;


import org.dami.pfa_back.Documents.History;
import org.dami.pfa_back.Repository.HistoryRepo;
import org.dami.pfa_back.Repository.SongRepo;
import org.dami.pfa_back.Repository.UserRepo;

import org.dami.pfa_back.Services.RecommendationConsumerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Random;
import java.util.stream.StreamSupport;

@SpringBootApplication
public class PfaBackApplication {
    public static void main(String[] args) {
        SpringApplication.run(PfaBackApplication.class, args);
    }
    //@Bean
    CommandLineRunner start(SongRepo repo) {
        return args ->{};

    }

}


