package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepo extends ElasticsearchRepository<User,String> {
}
