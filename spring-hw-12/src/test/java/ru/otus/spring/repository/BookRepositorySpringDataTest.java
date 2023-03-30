package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.Book;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.spring.util.DataProducer.*;

@SuppressWarnings("SameParameterValue")
@DataMongoTest
@EnableMongock
@DisplayName("Репозиторий для работы с книгами должен")
class BookRepositorySpringDataTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private final static String BOOK_1_NAME = "Book 1";
    private final static String BOOK_5_NAME = "Book 5";
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String GENRE_5_NAME = "Genre 5";
    private final static String AUTHOR_1_NAME = "Author 1";

    @Test
    @DisplayName("проверять наличие книги по имени")
    void shouldCheckIfExistsByName() {
        assertTrue(bookRepository.existsByName(BOOK_1_NAME));
        assertFalse(bookRepository.existsByName(BOOK_5_NAME));
    }

    @Test
    @DisplayName("находить книги по имени жанра")
    void shouldFindByGenreName() {
        assertThat(bookRepository.findAllByGenresName(GENRE_1_NAME)).hasSameElementsAs(getAllBooksByGenreName(GENRE_1_NAME));
        assertEquals(Collections.emptyList(), bookRepository.findAllByGenresName(GENRE_5_NAME));
    }

    @Test
    @DirtiesContext
    @DisplayName("должен добавлять новую книгу")
    void shouldAdd() {
        Book book = bookRepository.save(new Book(BOOK_5_NAME, getAuthorByName(AUTHOR_1_NAME).orElseThrow(), getAllGenres()));
        assertEquals(book, mongoTemplate.findById(book.getId(), Book.class));
    }
}