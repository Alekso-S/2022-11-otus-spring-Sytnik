package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SameParameterValue")
@DisplayName("Репозиторий для работы с книгами должен")
@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa bookRepository;
    @Autowired
    private TestEntityManager entityManager;

    private final static long BOOK_5_ID = 5;
    private final static String BOOK_5_NAME = "Book 5";
    private final static long GENRE_1_ID = 1;
    private final static long GENRE_2_ID = 2;
    private final static long AUTHOR_1_ID = 1;

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnRecordsCnt() {
        assertEquals(getAll().size(), bookRepository.count());
    }

    @DisplayName("возвращать список всех книг")
    @Test
    void shouldReturnAllBooks() {
        assertIterableEquals(getAll(), bookRepository.getAll());
    }

    @DisplayName("возвращать книгу по идентификатору")
    @Test
    void shouldReturnBookById() throws BookNotFoundEx {
        assertEquals(getAll().get(0), bookRepository.getById(getAll().get(0).getId()));
    }

    @DisplayName("возвращать книгу по имени")
    @Test
    void shouldReturnBookByName() throws BookNotFoundEx {
        assertEquals(getAll().get(0), bookRepository.getByName(getAll().get(0).getName()));
    }

    @DisplayName("возвращать список книг по идентификатору жанра")
    @Test
    void shouldReturnBooksByGenreId() {
        assertIterableEquals(getByGenreId(GENRE_1_ID), bookRepository.getByGenreId(GENRE_1_ID));
    }

    @DisplayName("добавлять книгу")
    @DirtiesContext
    @Test
    void shouldCreateBook() {
        assertNull(entityManager.find(Book.class, BOOK_5_ID));
        Book book = bookRepository.add(new Book(BOOK_5_NAME, getAuthorById(AUTHOR_1_ID),
                List.of(getGenreById(GENRE_1_ID), getGenreById(GENRE_2_ID))));
        entityManager.flush();
        entityManager.clear();
        assertEquals(book, entityManager.find(Book.class, BOOK_5_ID));
    }

    @DisplayName("удалять книгу")
    @DirtiesContext
    @Test
    void shouldDeleteBook() {
        entityManager.persist(new Book(BOOK_5_NAME, getAuthorById(AUTHOR_1_ID),
                List.of(getGenreById(GENRE_1_ID), getGenreById(GENRE_2_ID))));
        entityManager.flush();
        entityManager.clear();
        Book book = entityManager.find(Book.class, BOOK_5_ID);
        assertNotNull(book);
        bookRepository.delete(book);
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(Book.class, BOOK_5_ID));
    }

    private List<Book> getAll() {
        return DataProducer.getAllBooks();
    }

    private List<Book> getByGenreId(long genreId) {
        return DataProducer.getBooksByGenreId(genreId);
    }

    private Author getAuthorById(long authorId) {
        return DataProducer.getAuthorById(authorId);
    }

    private Genre getGenreById(long genreId) {
        return DataProducer.getGenreById(genreId);
    }
}