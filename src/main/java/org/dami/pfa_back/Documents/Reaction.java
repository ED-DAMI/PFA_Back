package org.dami.pfa_back.Documents;

import org.dami.pfa_back.Documents.Enums.Emoji;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "reaction")
public class Reaction {
    @Id
    private String id;
    private String songId;
    private Emoji  emojis;
    private String reactorName;
    private Date   date;



    public String getSongId() {
        return songId;
    }

    public Reaction setSongId(String songId) {
        this.songId = songId;
        return this;
    }



    public String getId() {
        return id;
    }

    public Reaction setId(String id) {
        this.id = id;
        return this;
    }

    public Emoji getEmojis() {
        return emojis;
    }

    public Reaction setEmojis(Emoji emojis) {
        this.emojis = emojis;
        return this;
    }

    public String getReactorName() {
        return reactorName;
    }

    public Reaction setReactorName(String reactorName) {
        this.reactorName = reactorName;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Reaction setDate(Date date) {
        this.date = date;
        return this;
    }

    public Reaction(String id, Emoji emojis,String songId, String reactorName, Date date) {
        this.id = id;
        this.emojis = emojis;
        this.songId=songId;
        this.reactorName = reactorName;
        this.date = date;
    }
    public Reaction() {
    }
}
