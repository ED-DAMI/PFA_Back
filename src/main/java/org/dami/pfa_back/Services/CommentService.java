package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.Comment;
import org.dami.pfa_back.Repository.CommentReop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentReop commentReop;

    public CommentService(CommentReop commentReop) {
        this.commentReop = commentReop;
    }

    public Comment saveComment(Comment comment) {
       return commentReop.save(comment);
    }

    public List<Comment> getCommentsBySongId(String songId) {
          return commentReop.findBySongId(songId);
    }
}
