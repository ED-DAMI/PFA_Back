package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.Reaction;
import org.dami.pfa_back.Repository.ReactionRepo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReactionService {
    private final ReactionRepo reactionRepo;

    public ReactionService(ReactionRepo reactionRepo) {
        this.reactionRepo = reactionRepo;
    }

    public List<Reaction> getReactionsBySongId(String songId) {
         return reactionRepo.findBySongId(songId);

    }

    public Reaction saveReaction(Reaction reaction) {
        return reactionRepo.save(reaction);
    }
}
