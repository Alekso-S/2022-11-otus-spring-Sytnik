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
import ru.otus.spring.exception.AuthorNotFoundEx;
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
    private AuthorServiceImpl authorService;

    @MockBean
    private AuthorRepository authorRepository;

    private final static long AUTHOR_1_ID = 1;
    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String AUTHOR_3_NAME = "Author 3";
    private final static String SUCCESSFUL_ADD_MESSAGE = "Author added";
    private final static String SUCCESSFUL_DEL_MESSAGE = "Author deleted";

    @BeforeEach
    void setUp() throws AuthorNotFoundEx {
        when(authorRepository.count()).thenReturn((long) getAll().size());
        when(authorRepository.getAll()).thenReturn(getAll());
        when(authorRepository.getById(AUTHOR_1_ID)).thenReturn(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
        when(authorRepository.getByName(AUTHOR_1_NAME)).thenReturn(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowAuthorsCount() {
        assertEquals(getAll().size(), authorService.showAuthorsCount());
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldShowAllAuthors() {
        String authorsString = getAll().stream()
                .sorted(Comparator.comparing(Author::getId))
                .map(AuthorConverter::toString)
                .collect(Collectors.joining("\n"));
        assertEquals(authorsString, authorService.showAllAuthors());
    }

    @DisplayName("возвращать автора по идентификатору")
    @Test
    void shouldShowAuthorById() {
        assertEquals(AuthorConverter.toString(new Author(AUTHOR_1_ID, AUTHOR_1_NAME)),
                authorService.showAuthorById(AUTHOR_1_ID));
    }

    @DisplayName("возвращать автора по имени")
    @Test
    void shouldShowAuthorByName() {
        assertEquals(AuthorConverter.toString(new Author(AUTHOR_1_ID, AUTHOR_1_NAME)),
                authorService.showAuthorByName(AUTHOR_1_NAME));
    }

    @DisplayName("выводить сообщение об успешном добавлении автора")
    @Test
    void shouldShowAddAuthorMessage() {
        assertEquals(SUCCESSFUL_ADD_MESSAGE, authorService.addAuthor(AUTHOR_3_NAME));
    }

    @DisplayName("выводить сообщение об успешном удалении автора")
    @Test
    void shouldShowDelAuthorMessage() {
        assertEquals(SUCCESSFUL_DEL_MESSAGE, authorService.delAuthorByName(AUTHOR_1_NAME));
    }

    private List<Author> getAll() {
        return DataProducer.getAllAuthors();
    }
}