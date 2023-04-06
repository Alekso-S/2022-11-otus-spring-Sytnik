package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.spring.util.DataProducer.getAllGenres;

@DataMongoTest
@EnableMongock
@Import(GenreRepositoryImpl.class)
@DisplayName("Репозиторий для работы с жанрами должен")
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("считать число жанров")
    void shouldCount() {
        assertEquals(getAllGenres().size(), genreRepository.count());
    }

    @Test
    @DisplayName("находить все жанры")
    void shouldGetAll() {
        assertThat(genreRepository.findAll()).hasSameElementsAs(getAllGenres());
    }
}