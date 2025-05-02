package org.dami.pfa_back.Documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "stream-events")
public class StreamEvent {
    @Id
    private String id;
    private String userId;
    private String songId;
    private Date timestamp;
    private String device;
    private String location;

    public StreamEvent() {
    }

    public String getId() {
        return id;
    }

    public StreamEvent setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public StreamEvent setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getSongId() {
        return songId;
    }

    public StreamEvent setSongId(String songId) {
        this.songId = songId;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public StreamEvent setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getDevice() {
        return device;
    }

    public StreamEvent setDevice(String device) {
        this.device = device;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public StreamEvent setLocation(String location) {
        this.location = location;
        return this;
    }

    public StreamEvent(String id, String userId, String songId, Date timestamp, String device, String location) {
        this.id = id;
        this.userId = userId;
        this.songId = songId;
        this.timestamp = timestamp;
        this.device = device;
        this.location = location;
    }

    @Override
    public String toString() {
        return "StreamEvent{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", songId='" + songId + '\'' +
                ", timestamp=" + timestamp +
                ", device='" + device + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

