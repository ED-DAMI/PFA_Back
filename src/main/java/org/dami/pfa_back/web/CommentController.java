package org.dami.pfa_back.web;

import jakarta.json.Json;
import org.dami.pfa_back.DTO.CommentDto;
import org.dami.pfa_back.Documents.Comment;
import org.dami.pfa_back.Documents.Enums.Interaction;
import org.dami.pfa_back.Documents.User;
import org.dami.pfa_back.Documents.UserIntraction;
import org.dami.pfa_back.Repository.UserRepo;
import org.dami.pfa_back.Security.JwtUtil;
import org.dami.pfa_back.Services.CommentService;
import org.dami.pfa_back.Services.KafkaService;
import org.dami.pfa_back.Services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")

public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final SongService songService;
    private final KafkaService kafkaService;

    public CommentController(CommentService commentService, JwtUtil jwtUtil, UserRepo userRepo, SongService songService, KafkaService kafkaService) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.songService = songService;
        this.kafkaService = kafkaService;
    }

    @GetMapping("/{songId}")
    public List<Comment> getComments(@PathVariable String songId) {
        return commentService.getCommentsBySongId(songId);
    }

    @PostMapping("/{songId}")
    public Comment addComment(@RequestHeader("Authorization") String auth,@PathVariable String songId ,@RequestBody CommentDto commentDto) {
        String username = getUsername(auth);

        Comment comment = new Comment(null, username, songId, commentDto.getText(), new Date(),false);
        songService.IncrementCommentaire(songId);
        Comment saved = commentService.saveComment(comment);
        kafkaService.sendUserInteraction(new UserIntraction(getUserId(auth),songId,saved.getId(), Interaction.COMMENT));

        return saved;
    }

    private String getUsername(String auth) {

        String token = auth.substring(7);
        String userId = jwtUtil.extractUserId(token);
        Optional<User> OptinnalUser = userRepo.findById(userId);
        var user=OptinnalUser.get();
        String username = user.getUsername();
        return username;
    }
    private String getUserId(String auth) {

        String token = auth.substring(7);
        String userId = jwtUtil.extractUserId(token);

        return userId;
    }
}

