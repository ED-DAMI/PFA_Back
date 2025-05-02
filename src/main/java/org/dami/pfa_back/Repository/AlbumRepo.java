package org.dami.pfa_back.Repository;

import org.dami.pfa_back.Documents.Album;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AlbumRepo extends ElasticsearchRepository<Album,String> {
}
