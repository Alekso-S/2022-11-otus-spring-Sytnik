package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.repository.AuthorRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.otus.spring.util.DataProducer.getAllAuthors;

@DisplayName("Сервис работы с авторами должен")
@SpringBootTest
@Import(AuthorServiceImpl.class)
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        when(authorRepository.count()).thenReturn((long) getAllAuthors().size());
        when(authorRepository.findAll()).thenReturn(getAllAuthors());
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowAuthorsCount() {
        assertEquals(getAllAuthors().size(), authorService.count());
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldShowAllAuthors() {
        List<AuthorDto> authors = getAllAuthors().stream()
                .map(AuthorDto::new)
                .sorted(Comparator.comparing(AuthorDto::getName))
                .collect(Collectors.toList());
        assertEquals(authors, authorService.getAll());
    }
}