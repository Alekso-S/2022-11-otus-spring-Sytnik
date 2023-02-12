package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByName(String name);

    boolean existsByName(String name);

    @EntityGraph(attributePaths = "author")
    List<Book> findAll();

    @EntityGraph(attributePaths = "author")
    List<Book> findAllByGenresId(long genreId);

    @EntityGraph(attributePaths = "author")
    List<Book> findAllByGenresName(String genreName);

    boolean existsByAuthorName(String authorName);

    boolean existsByGenresName(String genreName);
}
