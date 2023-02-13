package ru.otus.spring.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.mongock.changelog.util.DataProducer;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@ChangeLog
public class InitChangelog {

    private List<Book> books;

    @ChangeSet(order = "000", id = "drop-db", author = "Alekso-S", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "add-books", author = "Alekso-S")
    public void addBooks(BookRepository bookRepository) {
        books = bookRepository.saveAll(DataProducer.getAllBooks());
    }

    @ChangeSet(order = "002", id = "add-comments", author = "Alekso-S")
    public void addComments(CommentRepository commentRepository) {
        List<Comment> comments = DataProducer.getBookCommentsMap().entrySet().stream()
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
