package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Genre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface GenreRepo extends ElasticsearchRepository<Genre,String> {
    Optional<Genre> findByName(String genreName);
}
