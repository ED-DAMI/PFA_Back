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
    private String reactorId;
    private Date   date;

    @Override
    public String toString() {
        return "Reaction{" +
                "id='" + id + '\'' +
                ", songId='" + songId + '\'' +
                ", emojis=" + emojis +
                ", reactorId='" + reactorId + '\'' +
                ", date=" + date +
                '}';
    }

    public Reaction(String id, String songId, Emoji emojis, String reactorId, Date date) {
        this.id = id;
        this.songId = songId;
        this.emojis = emojis;
        this.reactorId = reactorId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Reaction setId(String id) {
        this.id = id;
        return this;
    }

    public String getSongId() {
        return songId;
    }

    public Reaction setSongId(String songId) {
        this.songId = songId;
        return this;
    }

    public Emoji getEmojis() {
        return emojis;
    }

    public Reaction setEmojis(Emoji emojis) {
        this.emojis = emojis;
        return this;
    }

    public String getReactorId() {
        return reactorId;
    }

    public Reaction setReactorId(String reactorId) {
        this.reactorId = reactorId;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Reaction setDate(Date date) {
        this.date = date;
        return this;
    }
}
