package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorNotFoundEx;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Сервис работы с авторами должен")
@SpringBootTest
@Import(AuthorServiceImpl.class)
class AuthorServiceImplTest {

    @Autowired
    private AuthorServiceImpl authorService;

    @MockBean
    private AuthorDao authorDao;
    @MockBean
    private GenreDao genreDao;
    @MockBean
    private BookDao bookDao;

    private final static long AUTHOR_1_ID = 1;
    private final static long AUTHOR_2_ID = 2;
    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String AUTHOR_2_NAME = "Author 2";
    private final static String AUTHOR_3_NAME = "Author 3";
    private final static String SUCCESSFUL_ADD_MESSAGE = "Author added";
    private final static String SUCCESSFUL_DEL_MESSAGE = "Author deleted";

    @BeforeEach
    void setUp() throws AuthorNotFoundEx {
        when(authorDao.count()).thenReturn(Long.valueOf(getAllAuthors().size()));
        when(authorDao.getAll()).thenReturn(getAllAuthors());
        when(authorDao.getById(AUTHOR_1_ID)).thenReturn(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
        when(authorDao.getByName(AUTHOR_1_NAME)).thenReturn(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
        when(authorDao.getByName(AUTHOR_3_NAME)).thenThrow(AuthorNotFoundEx.class);
    }

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldShowAuthorsCount() {
        assertEquals(getAllAuthors().size(), authorService.showAuthorsCount());
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldShowAllAuthors() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Author author : getAllAuthors()) {
            stringBuilder.append(AuthorConverter.toString(author)).append('\n');
        }
        assertEquals(stringBuilder.toString(), authorService.showAllAuthors());
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

    private List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(AUTHOR_1_ID, AUTHOR_1_NAME));
        authors.add(new Author(AUTHOR_2_ID, AUTHOR_2_NAME));
        return authors;
    }
}