package ru.otus.spring.mongock.changelogTest;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.model.mongo.MongoBook;
import ru.otus.spring.model.mongo.MongoComment;
import ru.otus.spring.repository.mongo.MongoBookRepository;
import ru.otus.spring.repository.mongo.MongoCommentRepository;
import ru.otus.spring.util.DataProducer;

import java.util.List;
import java.util.stream.Collectors;

@ChangeLog
public class InitChangelog {

    private List<MongoBook> books;

    @ChangeSet(order = "000", id = "drop-db", author = "Alekso-S", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "add-books", author = "Alekso-S")
    public void addBooks(MongoBookRepository bookRepository) {
        books = bookRepository.saveAll(DataProducer.getAllBooks());
    }

    @ChangeSet(order = "002", id = "add-comments", author = "Alekso-S")
    public void addComments(MongoCommentRepository commentRepository) {
        List<MongoComment> comments = DataProducer.getBookCommentsMap().entrySet().stream()
                .map(entry -> {
                    entry.getValue().forEach(comment -> comment.setBook(books.stream()
                            .filter(book -> book.getName().equals(entry.getKey()))
                            .findAny().orElse(null)));
                    return entry.getValue();
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
        commentRepository.saveAll(comments);
    }
}
