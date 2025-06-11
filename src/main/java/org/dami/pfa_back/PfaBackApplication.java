package org.dami.pfa_back;


import org.dami.pfa_back.DTO.RecommendationPayload;
import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.Documents.History;
import org.dami.pfa_back.Documents.Reaction;
import org.dami.pfa_back.Documents.Song;
import org.dami.pfa_back.Documents.User;
import org.dami.pfa_back.Repository.HistoryRepo;
import org.dami.pfa_back.Repository.ReactionRepo;
import org.dami.pfa_back.Repository.SongRepo;
import org.dami.pfa_back.Repository.UserRepo;

import org.dami.pfa_back.Services.RecommendationConsumerService;
import org.dami.pfa_back.Services.SongService;
import org.dami.pfa_back.web.RecommendationWebSocketController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SpringBootApplication
public class PfaBackApplication {
    public static void main(String[] args) {
        SpringApplication.run(PfaBackApplication.class, args);
    }
    @Bean
    CommandLineRunner start(UserRepo userRepo) {
        return args ->{
            List<User> allUsers = StreamSupport
                    .stream(userRepo.findAll().spliterator(), false)
                    .toList();

// Supprime tous les utilisateurs de la base
            userRepo.deleteAll();

// Filtrer les utilisateurs pour ne garder qu'un seul par email
            List<User> filteredList = allUsers.stream()
                    .filter(distinctByKey(User::getEmail))
                    .toList();

// Sauvegarde la liste filtrée
            userRepo.saveAll(filteredList);


        };

    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}


