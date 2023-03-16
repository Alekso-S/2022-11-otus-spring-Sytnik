package ru.otus.spring.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.security.SecurityConfig;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.util.DataProducer;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorRestController.class)
@Import(SecurityConfig.class)
@DisplayName("REST контроллер авторов должен")
class AuthorRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuthorService authorService;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("возвращать всех авторов")
    void shouldGetAll() throws Exception {
        List<AuthorDto> authors = AuthorConverter.toDto(DataProducer.getAllAuthors());
        when(authorService.getAll()).thenReturn(authors);

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authors)));
    }
}