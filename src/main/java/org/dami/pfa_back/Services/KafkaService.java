package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.UserIntraction; // Assurez-vous que ce chemin d'importation est correct
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String userInteractionTopic;


    public KafkaService(KafkaTemplate<String, String> kafkaTemplate,
                        @Value("${kafka.topic.user-interaction}") String userInteractionTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.userInteractionTopic = userInteractionTopic;
    }


    public void sendUserInteraction(UserIntraction interaction) {
        if (interaction == null) {
            logger.warn("Tentative d'envoyer un objet UserIntraction null à Kafka. Opération ignorée.");
            return;
        }

        String messagePayload = interaction.toString();
        String key = interaction.getUserId(); // Utilisation de userId comme clé de message Kafka

        logger.info("Envoi de UserIntraction au topic Kafka '{}' avec la clé '{}': {}",
                userInteractionTopic, key, messagePayload);

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(userInteractionTopic, key, messagePayload);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("Échec de l'envoi de UserIntraction au topic Kafka '{}' avec la clé '{}'. Contenu: {}",
                        userInteractionTopic, key, messagePayload, ex);
                // Vous pouvez envisager de relancer une exception personnalisée ici ou une autre gestion d'erreur
            } else {
                logger.info("UserIntraction envoyé avec succès au topic Kafka '{}', partition {}, offset {}. Contenu: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        messagePayload);
            }
        });
    }
}
