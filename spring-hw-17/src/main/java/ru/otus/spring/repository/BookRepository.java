package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {
    boolean existsByName(String name);

    List<Book> findAllByGenresName(String genreName);

    List<Book> findAllByAuthorName(String authorName);
}
