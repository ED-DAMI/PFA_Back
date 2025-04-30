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
@Document(indexName = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String role; // LISTENER, ARTIST, ADMIN
    private List<String> favorites;
    private List<String> playlists;
    private Date createdAt;
}

