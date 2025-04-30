package org.dami.pfa_back.Documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Document(indexName = "playlists")
public class Playlist {
    @Id
    private String id;
    private String name;
    private String description;
    private List<String> songs;
    private String ownerId;
    private boolean isPublic;
    private Date createdAt;
}

