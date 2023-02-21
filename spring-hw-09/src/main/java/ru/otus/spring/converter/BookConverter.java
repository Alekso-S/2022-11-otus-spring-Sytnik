package ru.otus.spring.converter;

import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookConverter {

    public static List<BookDto> toDto(List<Book> books) {
        return books.stream()
                .map(BookDto::new)
                .sorted(Comparator.comparing(BookDto::getName))
                .collect(Collectors.toList());
    }
}
