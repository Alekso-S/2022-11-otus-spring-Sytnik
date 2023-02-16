package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.repository.BookRepository;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    @Override
    public long showCount() {
        return bookRepository.count();
    }

    @Override
    public String showById(String id) {
        try {
            return BookConverter.toString(bookRepository.findById(id).orElseThrow(BookNotFoundEx::new));
        } catch (BookNotFoundEx e) {
            logger.warn("Book with id={} was not found", id);
            return "Book not found";
        }
    }

    @Override
    public String showByName(String name) {
        try {
            return BookConverter.toString(bookRepository.findByName(name).orElseThrow(BookNotFoundEx::new));
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} was not found", name);
            return "Book not found";
        }
    }

    @Override
    public String showAll() {
        return BookConverter.toString(bookRepository.findAll());
    }

    @Override
    public String showAllByGenreName(String genreName) {
        return BookConverter.toString(bookRepository.findAllByGenresName(genreName));
    }

    @Override
    public String add(String name, String authorName, String... genreNames) {
        if (bookRepository.existsByName(name)) {
            logger.warn("Book with name={} already exists", name);
            return "Book already exists";
        }
        bookRepository.save(new Book(
                name,
                new Author(authorName),
                Arrays.stream(genreNames)
                        .map(Genre::new)
                        .collect(Collectors.toList())
        ));
        return "Book added";
    }

    @Override
    public String deleteByName(String name) {
        Book book;
        try {
            book = bookRepository.findByName(name).orElseThrow(BookNotFoundEx::new);
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} not found", name);
            return "Book not found";
        }
        bookRepository.deleteWithComments(book);
        return "Book deleted";
    }
}
