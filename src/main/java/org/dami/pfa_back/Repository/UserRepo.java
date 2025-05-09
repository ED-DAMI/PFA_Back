package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface UserRepo extends ElasticsearchRepository<User,String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String usename);
}
