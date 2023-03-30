package ru.otus.spring.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public List<AuthorDto> getAll() {
        return authorService.getAll();
    }
}
