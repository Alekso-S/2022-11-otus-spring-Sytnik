package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.util.DataProducer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
        when(authorRepository.count()).thenReturn((long) getAll().size());
        when(authorRepository.findAll()).thenReturn(getAll());
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowAuthorsCount() {
        assertEquals(getAll().size(), authorService.showCount());
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldShowAllAuthors() {
        String authorsString = getAll().stream()
                .sorted(Comparator.comparing(Author::getName))
                .map(AuthorConverter::toString)
                .collect(Collectors.joining("\n"));
        assertEquals(authorsString, authorService.showAll());
    }

    private List<Author> getAll() {
        return DataProducer.getAllAuthors();
    }
}