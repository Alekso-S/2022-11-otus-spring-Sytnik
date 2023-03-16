package ru.otus.spring.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreRestController.class)
@DisplayName("REST контроллер жанров должен")
class GenreRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    GenreService genreService;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("возвращать все жанры")
    void shouldGetAll() throws Exception {
        List<GenreDto> genres = GenreConverter.toDto(DataProducer.getAllGenres());
        when(genreService.getAll()).thenReturn(genres);

        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(genres)));
    }
}