package org.dami.pfa_back.web;

import org.dami.pfa_back.DTO.RecommendationPayload;
import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.Services.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
// import org.dami.pfa_back.service.RecommendationService; // Si vous avez un service pour générer les recommandations

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class RecommendationWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationWebSocketController.class);
    private static final String USER_SPECIFIC_QUEUE_DESTINATION = "/queue/songs";
    private final SimpMessagingTemplate messagingTemplate;


    public static final String USER_RECOMMENDATION_DESTINATION_SUFFIX = "/user/queue/songs";
    private final SongService songService;

    public RecommendationWebSocketController(SimpMessagingTemplate messagingTemplate, /*, RecommendationService recommendationService */SongService songService) {
        this.messagingTemplate = messagingTemplate;

        this.songService = songService;
    }







    // Injectez RecommendationService dans le constructeur et assignez-le
// private final RecommendationService recommendationService;

    public void sendRecommendationToUser(RecommendationPayload recommendationPayload) { // Plus besoin de passer le payload en argument

        String userId = recommendationPayload.getUserId();
        if (userId == null || userId.isEmpty()) {
            logger.warn("Cannot send WebSocket recommendation: userId is null or empty.");
            return;
        }

        // Supposons que recommendationService.generateRecommendations retourne List<SongDto>
        // List<SongDto> recommendedSongs = recommendationService.generateRecommendations(userId, 10);

        // Pour l'instant, utilisons votre logique existante mais avec le userId correct





       //payload.setMessage("Voici vos recommandations personnalisées !"); // Optionnel

        logger.info("Sending recommendation payload to user '{}' at user-specific destination '{}'",
                userId,USER_SPECIFIC_QUEUE_DESTINATION ); // Utilisez la destination corrigée

        try {
            messagingTemplate.convertAndSendToUser(
                    userId,
                    USER_SPECIFIC_QUEUE_DESTINATION,
                    recommendationPayload.getSongs()
            );
            logger.debug("Successfully sent recommendation to user {} with payload: {}", userId, recommendationPayload);
        } catch (Exception e) {
            logger.error("Error sending WebSocket recommendation to user {}: {}", userId, e.getMessage(), e);
        }
    }

    @MessageMapping("/songs/getAll") // Correspond à la demande du client
    public void getAllSongs(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        logger.info("SONG_WS_CONTROLLER: getAllSongs CALLED.");

        if (principal == null || principal.getName() == null) {
            String sessionId = headerAccessor.getSessionId();
            logger.warn("SONG_WS_CONTROLLER: Principal is NULL in getAllSongs for session ID: {}. User might not be properly authenticated.", sessionId);

            return;
        }
        String userId = principal.getName(); // C'est l'identifiant de l'utilisateur authentifié
        logger.info("SONG_WS_CONTROLLER: Request for all songs from user: {}", userId);

        try {

            List<SongDto> songsToReturn = songService.getAllSongsAsDto(userId).stream()
                    .limit(2)
                    .toList();


            logger.info("SONG_WS_CONTROLLER: Retrieved {} songs for user: {}. Sending to /user/{}/queue/songs",
                    songsToReturn.size(), userId, userId);

            // Le client Flutter s'abonne à "/user/queue/songs"
            messagingTemplate.convertAndSendToUser(userId, "/queue/songs", songsToReturn);

            logger.info("SONG_WS_CONTROLLER: Successfully sent {} songs to user '{}'.", songsToReturn.size(), userId);

        } catch (Exception e) {
            logger.error("SONG_WS_CONTROLLER: Error processing getAllSongs for user {}: {}", userId, e.getMessage(), e);
            messagingTemplate.convertAndSendToUser(userId, "/queue/songs",
                    Collections.singletonMap("error", "Server error while fetching songs: " + e.getMessage()));
        }
    }

}
