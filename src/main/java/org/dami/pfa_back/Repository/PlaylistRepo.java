package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Documents.Song;
import org.dami.pfa_back.Documents.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PlaylistRepo extends ElasticsearchRepository<Playlist,String> {
    List<Playlist> findByuserID(String user);

}
