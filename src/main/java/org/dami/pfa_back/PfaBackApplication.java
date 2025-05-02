package org.dami.pfa_back;

import org.dami.pfa_back.Documents.Song; // Importer la classe Song correcte
import org.dami.pfa_back.Repository.SongRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class PfaBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(PfaBackApplication.class, args);
    }

  //  @Bean
    CommandLineRunner start(SongRepo songRepo) { // Injection de SongRepo
        return args -> {

        };
    }
}
