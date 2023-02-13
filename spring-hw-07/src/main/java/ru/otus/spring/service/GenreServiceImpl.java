package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.GenreNotFoundEx;
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
    public String showById(long id) {
        try {
            return GenreConverter.toString(genreRepository.findById(id).orElseThrow(GenreNotFoundEx::new));
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with id={} was not found", id);
            return "Genre not found";
        }
    }

    @Override
    public String showByName(String name) {
        try {
            return GenreConverter.toString(genreRepository.findByName(name).orElseThrow(GenreNotFoundEx::new));
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with name={} was not found", name);
            return "Genre not found";
        }
    }

    @Override
    public String showAll() {
        return GenreConverter.toString(genreRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public String showAllByBookId(long bookId) {
        try {
            return GenreConverter.toString(
                    bookRepository.findById(bookId).orElseThrow(BookNotFoundEx::new).getGenres());
        } catch (BookNotFoundEx e) {
            logger.warn("Book with id={} was not found", bookId);
            return "Book not found";
        }
    }

    @Transactional(readOnly = true)
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

    @Transactional
    @Override
    public String add(String name) {
        if (genreRepository.existsByName(name)) {
            logger.warn("Genre with name={} already exists", name);
            return "Genre already exists";
        }
        genreRepository.save(new Genre(name));
        return "Genre added";
    }

    @Transactional
    @Override
    public String deleteByName(String name) {
        Genre genre;
        try {
            genre = genreRepository.findByName(name).orElseThrow(GenreNotFoundEx::new);
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with name={} not found", name);
            return "Genre not found";
        }
        if (bookRepository.existsByGenresName(name)) {

            logger.warn("Books by genre with name={} exist", name);
            return "Books by this genre exist";
        }
        genreRepository.delete(genre);
        return "Genre deleted";
    }
}
