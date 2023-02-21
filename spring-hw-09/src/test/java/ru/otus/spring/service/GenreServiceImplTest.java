package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.repository.GenreRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
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

    @BeforeEach
    void setUp() {
        when(genreRepository.count()).thenReturn(Long.valueOf(getAllGenres().size()));
        when(genreRepository.findAll()).thenReturn(getAllGenres());
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowGenresCount() {
        assertEquals(getAllGenres().size(), genreService.count());
    }

    @DisplayName("возвращать список всех жанров")
    @Test
    void showAllGenres() {
        List<GenreDto> genres = getAllGenres().stream()
                .map(GenreDto::new)
                .sorted(Comparator.comparing(GenreDto::getName))
                .collect(Collectors.toList());
        assertEquals(genres, genreService.getAll());
    }
}