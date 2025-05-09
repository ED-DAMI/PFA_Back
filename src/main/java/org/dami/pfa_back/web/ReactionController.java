package org.dami.pfa_back.web;

import org.dami.pfa_back.DTO.EmojiRequest;
import org.dami.pfa_back.Documents.Enums.Emoji;
import org.dami.pfa_back.Documents.Reaction;
import org.dami.pfa_back.Repository.ReactionRepo;
import org.dami.pfa_back.Repository.UserRepo;
import org.dami.pfa_back.Security.JwtUtil;
import org.dami.pfa_back.Services.ReactionService;
import org.dami.pfa_back.Services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/reactions")

public class ReactionController {

    private final ReactionService reactionService;
    private final SongService songService;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    public ReactionController(ReactionService reactionService, SongService songService, UserRepo userRepo, JwtUtil jwtUtil) {
        this.reactionService = reactionService;
        this.songService = songService;

        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{songId}")
    public List<Reaction> getReactions(@PathVariable String songId) {

        return reactionService.getReactionsBySongId(songId);
    }

    @PostMapping("/{songId}")
    public Reaction addReaction(@RequestHeader("Authorization") String auth,
                                @PathVariable String songId,
                                @RequestBody EmojiRequest emojiRequest) { // <--- Use the DTO
        String emojiSymbol = emojiRequest.getEmoji(); // This will correctly be "😢"
        Emoji emojiEnum = Emoji.fromSymbol(emojiSymbol); // Assuming Emoji.fromSymbol exists and works

        if (emojiEnum == null) {
            // It's good practice to let Spring handle this with a proper HTTP status.
            // Consider throwing a custom exception that maps to 400 Bad Request.
            throw new IllegalArgumentException("Invalid emoji symbol provided: " + emojiSymbol);
        }

        Reaction reactionToSave = new Reaction(null, emojiEnum, songId,getUsername(auth), new Date());
        // Ensure songId is associated with reactionToSave if necessary
        // reactionToSave.setSongId(songId); // If your Reaction entity has this
        songService.IncmenterTotaleReaction(songId);
        return reactionService.saveReaction(reactionToSave);
    }
    private String getUsername(String auth) {
        String token = auth.substring(7);
        String email = jwtUtil.extractEmail(token);
        String username= userRepo.findByEmail(email).get().getUsername();
        System.out.println(username);
        return username;
    }


}

