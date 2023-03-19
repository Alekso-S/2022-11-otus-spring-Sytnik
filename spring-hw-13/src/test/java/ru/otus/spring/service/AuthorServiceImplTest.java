package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.repository.AuthorRepository;

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

    @DisplayName("возвращать корректное число записей")
    @Test
    void shouldReturnAuthorsCount() {
        when(authorRepository.count()).thenReturn((long) getAllAuthors().size());

        assertEquals(getAllAuthors().size(), authorService.count());
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        when(authorRepository.findAll()).thenReturn(getAllAuthors());

        assertEquals(AuthorConverter.toDto(getAllAuthors()), authorService.getAll());
    }
}