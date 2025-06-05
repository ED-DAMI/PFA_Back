package org.dami.pfa_back.web;

import org.apache.catalina.connector.Response;
import org.dami.pfa_back.DTO.EmojiRequest;
import org.dami.pfa_back.Documents.Enums.Emoji;
import org.dami.pfa_back.Documents.Enums.Interaction;
import org.dami.pfa_back.Documents.Reaction;
import org.dami.pfa_back.Documents.UserIntraction;
import org.dami.pfa_back.Repository.ReactionRepo;
import org.dami.pfa_back.Repository.UserRepo;
import org.dami.pfa_back.Security.JwtUtil;
import org.dami.pfa_back.Services.KafkaService;
import org.dami.pfa_back.Services.ReactionService;
import org.dami.pfa_back.Services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/reactions")

public class ReactionController {
    private final ReactionService reactionService;
    private final SongService songService;

    private final JwtUtil jwtUtil;
    private final KafkaService kafkaService;

    public ReactionController(ReactionService reactionService, SongService songService, UserRepo userRepo, JwtUtil jwtUtil, KafkaService kafkaService) {
        this.reactionService = reactionService;
        this.songService = songService;
        this.jwtUtil = jwtUtil;
        this.kafkaService = kafkaService;
    }
    @GetMapping("/{songId}")
    public List<Reaction> getReactions(@PathVariable String songId) {
        return reactionService.getReactionsBySongId(songId);
    }

    @PostMapping("/{songId}")
    public ResponseEntity<Reaction> addReaction(@RequestHeader("Authorization") String auth,
                                      @PathVariable String songId,
                                      @RequestBody EmojiRequest emojiRequest) {
        String emojiSymbol = emojiRequest.getEmoji(); // This will correctly be "😢"
        Emoji emojiEnum = Emoji.fromSymbol(emojiSymbol); // Assuming Emoji.fromSymbol exists and works

        if (emojiEnum == null) {
            throw new IllegalArgumentException("Invalid emoji symbol provided: " + emojiSymbol);
        }
        String reactorId = getUserId(auth);
        Optional<Reaction>  Optreaction = reactionService.getReaction(reactorId, songId);
        if (!Optreaction.isPresent())
            return saveReaction(songId, emojiEnum, reactorId);

        Reaction reaction = Optreaction.get();
        if (reaction.getEmojis()==emojiEnum)
        {
            reactionService.deleteReaction(reaction);
            return ResponseEntity.ok(reaction);
        }
        UserIntraction userIntraction=new UserIntraction(reactorId,songId,reaction.getSongId(), Interaction.REACTION);
        kafkaService.sendUserInteraction(userIntraction);
        reactionService.deleteReaction(reaction);
        return saveReaction(songId, emojiEnum, reactorId);
    }

    private ResponseEntity<Reaction> saveReaction(String songId, Emoji emojiEnum, String reactorId) {
        Reaction reactionToSave = new Reaction(null, songId, emojiEnum, reactorId, new Date());
        songService.IncmenterTotaleReaction(songId);
        return ResponseEntity.ok(reactionService.saveReaction(reactionToSave));
    }

    @DeleteMapping("/{songId}")
    public void deleteReaction(@PathVariable String songId
            ,@RequestHeader(name = "Authorization") String auth){
        String reactorId = getUserId(auth);
        reactionService.deleteReaction(reactorId,songId);
    }



    private String getUserId(String auth) {
        String token = auth.substring(7);
        return jwtUtil.extractUserId(token);
    }

}

