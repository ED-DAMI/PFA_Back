package org.dami.pfa_back.web;

import jakarta.json.Json;
import org.dami.pfa_back.DTO.CommentDto;
import org.dami.pfa_back.Documents.Comment;
import org.dami.pfa_back.Repository.UserRepo;
import org.dami.pfa_back.Security.JwtUtil;
import org.dami.pfa_back.Services.CommentService;
import org.dami.pfa_back.Services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/comments")

public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final SongService songService;

    public CommentController(CommentService commentService, JwtUtil jwtUtil, UserRepo userRepo, SongService songService) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.songService = songService;
    }

    @GetMapping("/{songId}")
    public List<Comment> getComments(@PathVariable String songId) {
        return commentService.getCommentsBySongId(songId);
    }

    @PostMapping("/{songId}")
    public Comment addComment(@RequestHeader("Authorization") String auth,@PathVariable String songId ,@RequestBody CommentDto commentDto) {
        String username = getUsername(auth);
        Comment comment = new Comment(null, username, songId, commentDto.getText(), new Date());
        songService.IncrementCommentaire(songId);
        return commentService.saveComment(comment);
    }

    private String getUsername(String auth) {
        String token = auth.substring(7);
        String email = jwtUtil.extractEmail(token);
        String username= userRepo.findByEmail(email).get().getUsername();
        System.out.println(username);
        return username;
    }
}

