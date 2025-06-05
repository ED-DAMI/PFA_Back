package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Tag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TagRepo extends ElasticsearchRepository<Tag,String> {

}
