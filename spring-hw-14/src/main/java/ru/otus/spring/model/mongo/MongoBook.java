package ru.otus.spring.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("books")
public class MongoBook {

    @Id
    private String id;

    private String name;

    private MongoAuthor author;

    private List<MongoGenre> genres;

    public MongoBook(String name, MongoAuthor author, List<MongoGenre> genres) {
        this.name = name;
        this.author = author;
        this.genres = genres;
    }
}
