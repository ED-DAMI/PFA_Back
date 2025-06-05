package org.dami.pfa_back.Config; // Ou un autre package approprié pour les configurations

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.dami.pfa_back.DTO.RecommendationPayload; // Chemin vers votre DTO
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer; // Optionnel pour la gestion d'erreur de désérialisation

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Cette valeur est lue depuis application.properties
    @Value("${spring.kafka.consumer.properties.spring.json.trusted.packages}")
    private String trustedPackages;

    // Factory pour le listener qui consomme des RecommendationPayload (JSON)
    @Bean
    public ConsumerFactory<String, RecommendationPayload> recommendationConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Ou "latest" selon le besoin

        // Configure JsonDeserializer pour la valeur
        JsonDeserializer<RecommendationPayload> jsonDeserializer = new JsonDeserializer<>(RecommendationPayload.class);
        jsonDeserializer.addTrustedPackages(trustedPackages); // Utilise la propriété des packages de confiance
        jsonDeserializer.setUseTypeMapperForKey(false); // Si la clé n'est pas utilisée pour déterminer le type
        // Vous pouvez enlever les headers de type si le producteur ne les envoie pas (cas de notre script Python)
        jsonDeserializer.setRemoveTypeHeaders(true);


        // Optionnel: Wrapper avec ErrorHandlingDeserializer pour gérer les erreurs de désérialisation
        // ErrorHandlingDeserializer<RecommendationPayload> errorHandlingDeserializer =
        //        new ErrorHandlingDeserializer<>(jsonDeserializer);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(), // Pour la clé (userId est une String)
                jsonDeserializer // Utiliser jsonDeserializer directement
                // ou errorHandlingDeserializer si vous l'utilisez
        );
    }

    @Bean // Le nom de ce bean doit correspondre à celui utilisé dans containerFactory
    public ConcurrentKafkaListenerContainerFactory<String, RecommendationPayload> recommendationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RecommendationPayload> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(recommendationConsumerFactory());
        // Autres configurations possibles pour la factory :
        // factory.setConcurrency(3); // Nombre de threads consommateurs
        // factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE); // Si gestion manuelle des acquittements
        return factory;
    }
}
