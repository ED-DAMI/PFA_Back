package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Documents.Song;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PlaylistRepo extends ElasticsearchRepository<Playlist,String> {
}
