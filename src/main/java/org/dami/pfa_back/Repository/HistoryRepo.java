package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.History;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface HistoryRepo extends ElasticsearchRepository<History,String> {
    List<History> findByUserId(String userId);
    boolean existsBySongIdAndUserId(String songId, String userId);

    History findBySongIdAndUserId(String songId, String userId);
}
