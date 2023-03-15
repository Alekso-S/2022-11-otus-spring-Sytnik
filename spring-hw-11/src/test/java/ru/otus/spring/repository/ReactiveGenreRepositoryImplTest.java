package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.spring.util.DataProducer.getAllGenres;

@DataMongoTest
@EnableMongock
@Import(ReactiveGenreRepositoryImpl.class)
@DisplayName("Репозиторий для работы с жанрами должен")
class ReactiveGenreRepositoryImplTest {

    @Autowired
    private ReactiveGenreRepository genreRepository;

    @Test
    @DisplayName("возвращать число жанров")
    void shouldCount() {
        StepVerifier.create(genreRepository.count())
                .assertNext(count -> assertEquals(getAllGenres().size(), count))
                .verifyComplete();
    }

    @Test
    @DisplayName("возвращать все жанры")
    void shouldGetAll() {
        StepVerifier.create(genreRepository.findAll())
                .expectNextCount(getAllGenres().size())
                .thenConsumeWhile(author -> getAllGenres().contains(author))
                .verifyComplete();
    }
}