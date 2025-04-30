package org.dami.pfa_back.Documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Document(indexName = "stream-events")
public class StreamEvent {
    @Id
    private String id;
    private String userId;
    private String songId;
    private Date timestamp;
    private String device;
    private String location;

}

