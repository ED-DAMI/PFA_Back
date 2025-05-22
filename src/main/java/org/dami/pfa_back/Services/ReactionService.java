package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.Enums.Interaction;
import org.dami.pfa_back.Documents.Event;
import org.dami.pfa_back.Documents.Reaction;
import org.dami.pfa_back.Repository.ReactionRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReactionService {
    private final ReactionRepo reactionRepo;
    private final KafkaProducerService kafkaProducerService;

    public ReactionService(ReactionRepo reactionRepo, KafkaProducerService kafkaProducerService) {
        this.reactionRepo = reactionRepo;
        this.kafkaProducerService = kafkaProducerService;
    }
    public List<Reaction> getReactionsBySongId(String songId) {
         return reactionRepo.findBySongId(songId);
    }
    public Reaction saveReaction(Reaction reaction) {
        reaction=reactionRepo.save(reaction);
        Event event = new Event(reaction.getReactorId(), reaction.getSongId(),reaction.getId(), Interaction.REACTION);
        kafkaProducerService.send(event);
        return reaction;
    }
    public void deleteReaction(String reactorId, String songId) {
        reactionRepo.deleteByReactorIdAndSongId(reactorId,songId);
    }

    public Optional<Reaction> getReaction(String reactorId, String songId) {
       return reactionRepo.getByReactorIdAndSongId(reactorId,songId);
    }

    public void deleteReaction(Reaction reaction) {
        reactionRepo.delete(reaction);
    }
}
