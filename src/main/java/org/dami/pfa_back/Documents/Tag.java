package org.dami.pfa_back.Documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "tags")
public class Tag {
    @Id
    private String id;
    private String name;
    public Tag() {
    }
    public Tag(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public Tag(String name) {
        this.name=name;
    }

    public String getId() {
        return id;
    }

    public Tag setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Tag setName(String name) {
        this.name = name;
        return this;
    }


}
