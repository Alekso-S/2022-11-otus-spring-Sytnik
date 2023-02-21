package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.spring.util.DataProducer.getAllAuthors;

@DataMongoTest
@EnableMongock
@Import(AuthorRepositoryImpl.class)
@DisplayName("Репозиторий для работы с авторами должен")
class AuthorRepositorySpringDataTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("считать число авторов")
    void shouldCount() {
        assertEquals(getAllAuthors().size(), authorRepository.count());
    }

    @Test
    @DisplayName("находить всех авторов")
    void shouldGetAll() {
        assertThat(authorRepository.findAll()).hasSameElementsAs(getAllAuthors());
    }
}