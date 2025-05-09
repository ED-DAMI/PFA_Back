package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Reaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepo extends ElasticsearchRepository<Reaction,String> {
    List<Reaction> findBySongId(String songid);
}
