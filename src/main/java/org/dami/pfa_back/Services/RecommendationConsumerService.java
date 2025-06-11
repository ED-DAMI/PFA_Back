package org.dami.pfa_back.Services;

import org.dami.pfa_back.DTO.RecommendationPayload; // Le payload pour WebSocket (avec List<SongDto>)
import org.dami.pfa_back.DTO.RecommendationRequestPayload; // Le payload de Kafka (avec List<String> songIds)
import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.web.RecommendationWebSocketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationConsumerService.class);
    private final RecommendationWebSocketController webSocketController;
    private final SongService songService;


    public RecommendationConsumerService(
            RecommendationWebSocketController webSocketController,
            SongService songService) {
        this.webSocketController = webSocketController;
        this.songService = songService;
    }

    @KafkaListener(
            topics = "${kafka.topic.recommendation}",
            groupId = "recommendation-processor-group",
            containerFactory = "recommendationRequestKafkaListenerContainerFactory"
    )
    public void processRecommendationRequest(
            @Payload RecommendationRequestPayload kafkaPayload,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, // Added to get the actual topic name
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        logger.info("Requête de recommandation (avec IDs de chansons) reçue de Kafka: {}", kafkaPayload);
        // Utilisation de la variable 'topic' pour un logging correct du nom du topic
        logger.info("  Topic: {}, Partition: {}, Offset: {}, Clé (userId): {}", topic, partition, offset, key);

        String userId = kafkaPayload.getUserId();
        List<String> songIds = kafkaPayload.getSongIds();

        if (userId == null || userId.isEmpty()) {
            userId = key;
            logger.warn("UserId manquant dans le payload Kafka, utilisation de la clé Kafka comme userId: {}", userId);
        }

        if (userId != null && !userId.isEmpty() && songIds != null && !songIds.isEmpty()) {
            logger.info("Traitement de la recommandation pour l'utilisateur '{}' avec les IDs de chansons : {}", userId, songIds);

            List<SongDto> recommendedSongDtos = songService.getSongDtosByIds(songIds);

            if (recommendedSongDtos != null && !recommendedSongDtos.isEmpty()) { // Added null check for recommendedSongDtos
                RecommendationPayload webSocketPayload = new RecommendationPayload(userId, recommendedSongDtos);
                logger.info("Envoi de {} SongDto à l'utilisateur '{}' via WebSocket.", recommendedSongDtos.size(), userId);
                webSocketController.sendRecommendationToUser(webSocketPayload);
            } else {
                logger.warn("Aucun détail de chanson trouvé pour les IDs {} pour l'utilisateur {}. Rien à envoyer via WebSocket.", songIds, userId);
            }
        } else {
            logger.warn("Requête de recommandation invalide reçue de Kafka: userId ou songIds manquants. Payload: {}", kafkaPayload);
        }
    }

}
