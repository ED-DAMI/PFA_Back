package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.StreamEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StreamEventRepo extends ElasticsearchRepository<StreamEvent,String> {
}
