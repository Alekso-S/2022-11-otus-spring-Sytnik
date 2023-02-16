package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    private static final Logger logger = LoggerFactory.getLogger(GenreServiceImpl.class);

    @Override
    public long showCount() {
        return genreRepository.count();
    }

    @Override
    public String showAll() {
        return GenreConverter.toString(genreRepository.findAll());
    }

    @Override
    public String showAllByBookId(String bookId) {
        try {
            return GenreConverter.toString(
                    bookRepository.findById(bookId).orElseThrow(BookNotFoundEx::new).getGenres());
        } catch (BookNotFoundEx e) {
            logger.warn("Book with id={} was not found", bookId);
            return "Book not found";
        }
    }

    @Override
    public String showAllByBookName(String bookName) {
        try {
            return GenreConverter.toString(
                    bookRepository.findByName(bookName).orElseThrow(BookNotFoundEx::new).getGenres());
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} was not found", bookName);
            return "Book not found";
        }
    }
}
