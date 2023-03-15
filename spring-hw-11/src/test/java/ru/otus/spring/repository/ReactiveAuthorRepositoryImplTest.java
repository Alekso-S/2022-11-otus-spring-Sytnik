package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.spring.util.DataProducer.getAllAuthors;

@DataMongoTest
@EnableMongock
@Import(ReactiveAuthorRepositoryImpl.class)
@DisplayName("Репозиторий для работы с авторами должен")
class ReactiveAuthorRepositoryImplTest {

    @Autowired
    private ReactiveAuthorRepository authorRepository;

    @Test
    @DisplayName("возвращать число авторов")
    void shouldCount() {
        StepVerifier.create(authorRepository.count())
                .assertNext(count -> assertEquals(getAllAuthors().size(), count))
                .verifyComplete();
    }

    @Test
    @DisplayName("возвращать всех авторов")
    void shouldGetAll() {
        StepVerifier.create(authorRepository.findAll())
                .expectNextCount(getAllAuthors().size())
                .thenConsumeWhile(author -> getAllAuthors().contains(author))
                .verifyComplete();
    }
}