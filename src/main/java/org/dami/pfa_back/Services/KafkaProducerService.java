package org.dami.pfa_back.Services;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.dami.pfa_back.Documents.Event;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.Future;
@Service
public class KafkaProducerService {
    private final KafkaProducer<String, String> producer;
    private final String topicName="user-action";
    private final String bootstrapServers="localhost:9092";
    private static long cout=1;

    public KafkaProducerService() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producer = new KafkaProducer<>(props);
    }

    public void sendEvent(String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);
        try {
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata metadata = future.get(); // optionnel, si tu veux bloquer jusqu’à envoi réussi
           System.out.printf("Message envoyé à %s [partition=%d, offset=%d]%n "+value,metadata.topic(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        producer.close();
    }
    public void send(Event event){
        this.sendEvent("user-action"+cout,event.toString());
    }



//    public static void main(String[] args) {
//        KafkaProducerService producer = new KafkaProducerService("localhost:9092", "user-action");
//        producer.sendEvent("commande1", "{\"id\":1,\"produit\":\"chaise\"}");
//        producer.sendEvent("commande2", "{\"id\":2,\"produit\":\"table\"}");
//        producer.close();
//    }
}
