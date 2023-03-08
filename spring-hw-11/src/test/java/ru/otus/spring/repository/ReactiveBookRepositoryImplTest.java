package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;
import ru.otus.spring.domain.Book;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.otus.spring.util.DataProducer.*;

@DataMongoTest
@EnableMongock
@DisplayName("Репозиторий для работы с книгами должен")
class ReactiveBookRepositoryImplTest {

    @Autowired
    private ReactiveBookRepository bookRepository;
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private final static String BOOK_1_NAME = "Book 1";
    private final static String BOOK_5_NAME = "Book 5";
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String GENRE_5_NAME = "Genre 5";
    private final static String AUTHOR_1_NAME = "Author 1";

    @Test
    @DisplayName("проверять наличие книги по имени")
    void shouldCheckIfExistsByName() {
        StepVerifier.create(bookRepository.existsByName(BOOK_1_NAME))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
        StepVerifier.create(bookRepository.existsByName(BOOK_5_NAME))
                .assertNext(Assertions::assertFalse)
                .verifyComplete();
    }

    @Test
    @DisplayName("находить книги по имени жанра")
    void shouldFindByGenreName() {
        StepVerifier.create(bookRepository.findAllByGenresName(GENRE_1_NAME))
                .expectNextCount(getAllBooksByGenreName(GENRE_1_NAME).size())
                .thenConsumeWhile(book -> getAllBooksByGenreName(GENRE_1_NAME).contains(book))
                .verifyComplete();
        StepVerifier.create(bookRepository.findAllByGenresName(GENRE_5_NAME))
                .verifyComplete();
    }

    @Test
    @DirtiesContext
    @DisplayName("должен добавлять новую книгу")
    void shouldAdd() {
        List<Book> books = new ArrayList<>();
        StepVerifier.create(bookRepository.save(
                        new Book(BOOK_5_NAME, getAuthorByName(AUTHOR_1_NAME).orElseThrow(), getAllGenres())))
                .recordWith(() -> books)
                .assertNext(book -> assertNotNull(book.getId()))
                .verifyComplete();
        StepVerifier.create(mongoTemplate.findById(books.get(0).getId(), Book.class))
                .assertNext(book -> assertEquals(books.get(0), book))
                .verifyComplete();
    }
}