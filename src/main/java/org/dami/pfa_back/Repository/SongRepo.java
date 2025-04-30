package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Song;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SongRepo extends ElasticsearchRepository<Song,String> {
}
