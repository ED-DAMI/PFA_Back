package org.dami.pfa_back.Controllers; // Adaptez le package

import org.dami.pfa_back.DTO.RecommendationPayload; // Votre DTO existant
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

// Modèle de message simple pour un "hello" de test
class HelloMessage {
    private String name;
    public HelloMessage() {}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

class Greeting {
    private String content;
    public Greeting(String content) { this.content = content; }
    public String getContent() { return content; }
}

@Controller
public class RecommendationWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationWebSocketController.class);

    private final SimpMessagingTemplate messagingTemplate;


    public RecommendationWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/recommendation")
    @SendTo("/topic/recommendation")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // Simule un délai
        logger.info("Message 'hello' reçu de : {}", message.getName());
        return new Greeting("Bonjour, " + message.getName() + "!");
    }


    public void sendRecommendationToUser(String userId, RecommendationPayload payload) {
        if (userId == null || userId.isEmpty()) {
            logger.warn("Tentative d'envoyer une recommandation sans userId.");
            return;
        }

        String destination = "/user/" + userId + "/queue/recommendations";
        logger.info("Envoi de la recommandation à l'utilisateur '{}' sur la destination '{}'", userId, destination);
        messagingTemplate.convertAndSend(destination, payload);

        logger.info("Recommandation envoyée : {}", payload);
    }
}
