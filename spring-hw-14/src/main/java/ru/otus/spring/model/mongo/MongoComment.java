package ru.otus.spring.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("comments")
public class MongoComment {

    @Id
    private String id;

    @DBRef
    private MongoBook book;

    private String text;

    public MongoComment(String name) {
        this.text = name;
    }
}
