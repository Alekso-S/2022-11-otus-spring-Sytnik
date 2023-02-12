package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.util.DataProducer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SameParameterValue")
@DisplayName("Репозиторий для работы с книгами должен")
@DataJpaTest
class BookRepositorySpringDataTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final static String BOOK_1_NAME = "Book 1";
    private final static String BOOK_5_NAME = "Book 5";
    private final static long GENRE_1_ID = 1;
    private final static long GENRE_5_ID = 5;
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String GENRE_5_NAME = "Genre 5";
    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String AUTHOR_3_NAME = "Author 3";
    private final static long AUTHOR_1_ID = 1;

    @Test
    @DisplayName("находить книгу по имени")
    void shouldFindByName() {
        assertEquals(getByName(BOOK_1_NAME),bookRepository.findByName(BOOK_1_NAME));
        assertEquals(Optional.empty(),bookRepository.findByName(BOOK_5_NAME));
    }

    @Test
    @DisplayName("проверять наличие книги по имени")
    void shouldCheckIfExistsByName() {
        assertTrue(bookRepository.existsByName(BOOK_1_NAME));
        assertFalse(bookRepository.existsByName(BOOK_5_NAME));
    }

    @Test
    @DisplayName("находить книги по идентификатору жанра")
    void shouldFindByGenreId() {
        assertEquals(getAllByGenreId(GENRE_1_ID), bookRepository.findAllByGenresId(GENRE_1_ID));
        assertEquals(Collections.emptyList(), bookRepository.findAllByGenresId(GENRE_5_ID));
    }

    @Test
    @DisplayName("находить книги по имени жанра")
    void shouldFindByGenreName() {
        assertEquals(getAllByGenreName(GENRE_1_NAME), bookRepository.findAllByGenresName(GENRE_1_NAME));
        assertEquals(Collections.emptyList(), bookRepository.findAllByGenresName(GENRE_5_NAME));
    }

    @Test
    @DisplayName("проверять существование книг по имени автора")
    void shouldCheckIfExistsByAuthorName() {
        assertTrue(bookRepository.existsByAuthorName(AUTHOR_1_NAME));
        assertFalse(bookRepository.existsByAuthorName(AUTHOR_3_NAME));
    }

    @Test
    @DisplayName("проверять существование книг по названию жанра")
    void shouldCheckIfExistsByGenreName() {
        assertTrue(bookRepository.existsByGenresName(GENRE_1_NAME));
        assertFalse(bookRepository.existsByGenresName(GENRE_5_NAME));
    }

    @Test
    @DirtiesContext
    @DisplayName("должен добавлять новую книгу")
    void shouldAdd() {
        Book book = bookRepository.save(new Book(BOOK_5_NAME, getAuthorById(AUTHOR_1_ID).orElseThrow(), getAllGenres()));
        assertEquals(book, entityManager.find(Book.class, book.getId()));
    }

    private Optional<Book> getByName(String bookName) {
        return DataProducer.getBookByName(bookName);
    }

    private List<Book> getAllByGenreId(long genreId) {
        return DataProducer.getAllBooksByGenreId(genreId);
    }

    private List<Book> getAllByGenreName(String genreName) {
        return DataProducer.getAllBooksByGenreName(genreName);
    }

    private Optional<Author> getAuthorById(long authorId) {
        return DataProducer.getAuthorById(authorId);
    }

    private List<Genre> getAllGenres() {
        return DataProducer.getAllGenres();
    }
}