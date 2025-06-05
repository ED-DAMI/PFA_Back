package org.dami.pfa_back.Services; // Ou un autre package approprié

import org.dami.pfa_back.DTO.RecommendationPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RecommendationConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationConsumerService.class);

    // Le nom du topic est lu depuis application.properties
    // Le groupId est défini ici, ou peut être lu depuis application.properties
    // (par ex., groupId = "${spring.kafka.consumer.reco-group-id}")
    @KafkaListener(
            topics = "${kafka.topic.recommendation}",
            groupId = "recommendation-listener-group", // Choisissez un ID de groupe unique pour ce listener
            containerFactory = "recommendationKafkaListenerContainerFactory" // Référence à une factory spécifique
    )
    public void consumeRecommendation(
            @Payload RecommendationPayload payload, // Le message désérialisé
            @Header(KafkaHeaders.RECEIVED_KEY) String key, // La clé du message (userId)
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        logger.info("Message de recommandation reçu du topic '{}':", "${kafka.topic.recommendation}");
        logger.info("  Clé (userId): {}", key);
        logger.info("  Partition: {}", partition);
        logger.info("  Offset: {}", offset);
        logger.info("  Payload: {}", payload.toString()); // Utilise le toString() de RecommendationPayload

        // Maintenant, vous pouvez traiter le payload de recommandation :
        // Par exemple :
        // 1. Envoyer ces recommandations à un utilisateur via WebSockets.
        // 2. Stocker ces recommandations pré-calculées dans une base de données/cache.
        // 3. Déclencher d'autres logiques basées sur ces recommandations.

        String userId = payload.getUserId();
        if (userId != null && !userId.isEmpty()) {
            logger.info("Recommandations pour l'utilisateur '{}' : {} chansons.", userId,
                    (payload.getSongIds() != null ? payload.getSongIds().size() : 0));
            if (payload.getSongIds() != null && !payload.getSongIds().isEmpty()) {
                payload.getSongIds().forEach(songId -> logger.info("  - Chanson recommandée : {}", songId));
            } else {
                logger.info("  Aucune chanson spécifique dans cette recommandation.");
            }
        } else {
            logger.warn("Message de recommandation reçu sans userId valide dans le payload.");
        }

        // Exemple de traitement :
        // recommendationProcessingService.process(payload);
    }
}
