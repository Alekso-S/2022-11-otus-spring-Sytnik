package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.otus.spring.util.DataProducer.getAllBooks;
import static ru.otus.spring.util.DataProducer.getAllGenres;

@SuppressWarnings("SameParameterValue")
@DisplayName("Сервис работы с жанрами должен")
@SpringBootTest
@Import(GenreServiceImpl.class)
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    private static final long DELAY = 1500;
    private static final long DEFAULT_COUNT = -1;
    private static final Genre DEFAULT_GENRE = new Genre("Error");

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnGenresCountValid() {
        when(genreRepository.count()).thenReturn(Long.valueOf(getAllGenres().size()));

        assertEquals(getAllGenres().size(), genreService.count());
    }

    @DisplayName("возвращать заменённое число записей при некорректной работе источника")
    @Test
    void shouldReturnGenresCountInvalid() {
        Mockito.doAnswer(new AnswersWithDelay(DELAY, invocation -> (long) getAllBooks().size())).when(genreRepository).count();

        assertEquals(DEFAULT_COUNT, genreService.count());
    }

    @DisplayName("возвращать корректный список всех жанров")
    @Test
    void shouldReturnAllGenresValid() {
        when(genreRepository.findAll()).thenReturn(getAllGenres());

        assertEquals(GenreConverter.toDto(getAllGenres()), genreService.getAll());
    }

    @DisplayName("возвращать заменённый список всех жанров при некорректной работе источника")
    @Test
    void shouldReturnAllGenresInvalid() {
        Mockito.doAnswer(new AnswersWithDelay(DELAY, invocation -> (long) getAllBooks().size())).when(genreRepository).findAll();

        assertEquals(List.of(DEFAULT_GENRE.toDto()), genreService.getAll());
    }
}