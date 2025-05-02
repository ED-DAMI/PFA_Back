package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Artist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ArtistRepo extends ElasticsearchRepository<Artist,String> {
    Optional<Artist> findByName(String artistName);
}
