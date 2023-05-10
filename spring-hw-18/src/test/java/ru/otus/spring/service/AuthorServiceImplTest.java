package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.otus.spring.util.DataProducer.getAllAuthors;
import static ru.otus.spring.util.DataProducer.getAllBooks;

@DisplayName("Сервис работы с авторами должен")
@SpringBootTest
@Import(AuthorServiceImpl.class)
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    private static final long DELAY = 1500;
    private static final long DEFAULT_COUNT = -1;
    private static final Author DEFAULT_AUTHOR = new Author("Error");

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnAuthorsCountValid() {
        when(authorRepository.count()).thenReturn((long) getAllAuthors().size());

        assertEquals(getAllAuthors().size(), authorService.count());
    }

    @DisplayName("возвращать заменённое число записей при некорректной работе источника")
    @Test
    void shouldReturnAuthorsCountInvalid() {
        Mockito.doAnswer(new AnswersWithDelay(DELAY, invocation -> (long) getAllBooks().size())).when(authorRepository).count();

        assertEquals(DEFAULT_COUNT, authorService.count());
    }

    @DisplayName("возвращать корректный список всех авторов")
    @Test
    void shouldReturnAllAuthorsValid() {
        when(authorRepository.findAll()).thenReturn(getAllAuthors());

        assertEquals(AuthorConverter.toDto(getAllAuthors()), authorService.getAll());
    }

    @DisplayName("возвращать заменённый список всех авторов при некорректной работе источника")
    @Test
    void shouldReturnAllAuthorsInvalid() {
        Mockito.doAnswer(new AnswersWithDelay(DELAY, invocation -> (long) getAllBooks().size())).when(authorRepository).findAll();

        assertEquals(List.of(DEFAULT_AUTHOR.toDto()), authorService.getAll());
    }
}