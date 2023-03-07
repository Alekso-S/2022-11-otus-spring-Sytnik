package ru.otus.spring.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.rest.BookRestDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
@DisplayName("REST контроллер книг должен")
class BookRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BookService bookService;

    private final static String AUTHOR_1_NAME = "Author 1";
    private final static String GENRE_1_NAME = "Genre 1";
    private final static String BOOK_5_NAME = "Book 5";
    private final static String BOOK_5_ID = "5";
    private final static String BOOK_1_ID = "1";

    @Test
    @DisplayName("возвращать все книги")
    void shouldGetAll() throws Exception {
        List<BookDto> books = BookConverter.toDto(DataProducer.getAllBooks());
        when(bookService.getAll()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    @DisplayName("возвращать книги выбранного автора")
    void shouldGetAllByAuthorName() throws Exception {
        List<BookDto> books = BookConverter.toDto(DataProducer.getAllBooksByAuthorName(AUTHOR_1_NAME));
        when(bookService.getAllByAuthorName(AUTHOR_1_NAME)).thenReturn(books);

        mockMvc.perform(get("/api/books?author={author}", AUTHOR_1_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    @DisplayName("возвращать книги выбранного жанра")
    void shouldGetAllByGenreName() throws Exception {
        List<BookDto> books = BookConverter.toDto(DataProducer.getAllBooksByGenreName(GENRE_1_NAME));
        when(bookService.getAllByGenreName(GENRE_1_NAME)).thenReturn(books);

        mockMvc.perform(get("/api/books?genre={genre}", GENRE_1_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    @DisplayName("добавлять и возвращать книгу")
    void shouldAddAndReturn() throws Exception {
        BookRestDto bookRestDto = new BookRestDto(BOOK_5_NAME, AUTHOR_1_NAME, GENRE_1_NAME);
        BookDto bookDto = new BookDto(
                new Book(BOOK_5_ID, BOOK_5_NAME, new Author(AUTHOR_1_NAME), List.of(new Genre(GENRE_1_NAME))));
        when(bookService.add(BOOK_5_NAME, AUTHOR_1_NAME, GENRE_1_NAME)).thenReturn(bookDto);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDto)));
    }

    @Test
    @DisplayName("удалять книгу")
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/books/{book}", BOOK_1_ID))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteById(BOOK_1_ID);
    }
}