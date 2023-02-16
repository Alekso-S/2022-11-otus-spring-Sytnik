package ru.otus.spring.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableMongock
@Import(GenreRepositoryImpl.class)
@DisplayName("Репозиторий для работы с жанрами должен")
class GenreRepositorySpringDataTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("считать число жанров")
    void shouldCount() {
        assertEquals(getAll().size(), genreRepository.count());
    }

    @Test
    @DisplayName("находить все жанры")
    void shouldGetAll() {
        assertThat(genreRepository.findAll()).hasSameElementsAs(getAll());
    }

    private List<Genre> getAll() {
        return DataProducer.getAllGenres();
    }
}