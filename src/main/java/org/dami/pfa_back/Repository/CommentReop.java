package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Collection;
import java.util.List;

public interface CommentReop extends ElasticsearchRepository<Comment,String> {
    Collection<Comment> findByAuthor(String Author);
    List<Comment> findBySongId(String songId);

}
