package org.userway.assignment.litstull.data.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
@Data
@NoArgsConstructor
@Document(collection = "links")
public class LinkData implements Serializable {

    @Id
    String id;

    String origin;

    @Indexed(unique = true)
    String link;

    public LinkData(String origin, String link) {
        this.origin = origin;
        this.link = link;
    }
}
