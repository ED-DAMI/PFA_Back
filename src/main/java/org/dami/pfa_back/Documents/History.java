package org.dami.pfa_back.Documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "history")
public class History {
    @Id
    private String id;
    private String userId;
    private String songId;
    private  int durationListenedSeconds;
    private Date date;

    public int getDurationListenedSeconds() {
        return durationListenedSeconds;
    }

    public History setDurationListenedSeconds(int durationListenedSeconds) {
        this.durationListenedSeconds = durationListenedSeconds;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public History setDate(Date date) {
        this.date = date;
        return this;
    }

    public History() {
    }

    public History(String id, String userId, String songId, int durationListenedSeconds, Date date) {
        this.id = id;
        this.userId = userId;
        this.songId = songId;
        this.durationListenedSeconds = durationListenedSeconds;
        this.date = date;
    }

    @Override
    public String toString() {
        return "History{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", songId='" + songId + '\'' +
                ", durationListenedSeconds=" + durationListenedSeconds +
                ", date=" + date +
                '}';
    }

    public String getId() {
        return id;
    }

    public History setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public History setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getSongId() {
        return songId;
    }

    public History setSongId(String songId) {
        this.songId = songId;
        return this;
    }
}
