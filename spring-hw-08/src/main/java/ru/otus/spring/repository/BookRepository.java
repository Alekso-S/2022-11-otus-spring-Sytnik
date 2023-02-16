package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {

    Optional<Book> findByName(String name);

    boolean existsByName(String name);

    List<Book> findAllByGenresName(String genreName);

    boolean existsByAuthorName(String authorName);

    boolean existsByGenresName(String genreName);
}
