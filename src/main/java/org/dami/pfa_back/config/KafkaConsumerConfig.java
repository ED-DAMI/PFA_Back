package org.dami.pfa_back.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.dami.pfa_back.DTO.RecommendationRequestPayload; // Le DTO pour les messages Kafka
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.properties.spring.json.trusted.packages}")
    private String trustedPackages; // Doit inclure org.dami.pfa_back.DTO et java.util.List

    // Factory pour le listener qui consomme des RecommendationRequestPayload (JSON avec song IDs)
    @Bean
    public ConsumerFactory<String, RecommendationRequestPayload> recommendationRequestConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<RecommendationRequestPayload> deserializer = new JsonDeserializer<>(RecommendationRequestPayload.class);
        // Assurez-vous que trustedPackages dans application.properties inclut le package de RecommendationRequestPayload
        // par ex: org.dami.pfa_back.DTO,org.dami.pfa_back.Models,java.util.List
        // ou plus simplement: org.dami.pfa_back.*,java.util.List
        deserializer.addTrustedPackages(trustedPackages);
        deserializer.setUseTypeMapperForKey(false);
        deserializer.setRemoveTypeHeaders(true); // Si le producteur Python n'envoie pas d'en-têtes de type Spring

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean // Le nom de ce bean doit correspondre à celui utilisé dans containerFactory
    public ConcurrentKafkaListenerContainerFactory<String, RecommendationRequestPayload> recommendationRequestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RecommendationRequestPayload> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(recommendationRequestConsumerFactory());
        return factory;
    }

    // L'ancienne factory "recommendationKafkaListenerContainerFactory" (si elle désérialisait en RecommendationPayload avec List<SongDto>)
    // n'est plus nécessaire pour un @KafkaListener si aucun listener ne l'utilise directement.
    // Elle était pour un scénario où le message Kafka contenait déjà List<SongDto>.
}
