package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.Comment;

import org.dami.pfa_back.Documents.UserIntraction;
import org.dami.pfa_back.Repository.CommentReop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentReop commentReop;
    private final KafkaService kafkaService;

    public CommentService(CommentReop commentReop, KafkaService kafkaService) {
        this.commentReop = commentReop;
        this.kafkaService = kafkaService;
    }
    public Comment saveComment(Comment comment) {
        Comment saved = commentReop.save(comment);

        return saved;
    }
    public List<Comment> getCommentsBySongId(String songId) {
          return commentReop.findBySongId(songId);
    }
}
