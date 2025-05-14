package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.history;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface historyRepo extends ElasticsearchRepository<history,String> {
    List<history> findByUserId(String userId);
}
