package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CommentReop extends ElasticsearchRepository<Comment,String> {
}
