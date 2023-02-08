package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.domain.Genre;
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
    public long showGenresCount() {
        return genreRepository.count();
    }

    @Override
    public String showGenreById(long id) {
        try {
            return GenreConverter.toString(genreRepository.getById(id));
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with id={} was not found", id);
            return "Genre not found";
        }
    }

    @Override
    public String showGenreByName(String name) {
        try {
            return GenreConverter.toString(genreRepository.getByName(name));
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with name={} was not found", name);
            return "Genre not found";
        }
    }

    @Override
    public String showAllGenres() {
        return GenreConverter.toString(genreRepository.getAll());
    }

    @Override
    public String showGenresByBookId(long bookId) {
        if (!bookRepository.checkExistenceById(bookId)) {
            logger.warn("Book with id={} was not found", bookId);
            return "Book not found";
        }
        return GenreConverter.toString(genreRepository.getByBookId(bookId));
    }

    @Override
    public String showGenresByBookName(String bookName) {
        if (!bookRepository.checkExistenceByName(bookName)) {
            logger.warn("Book with name={} was not found", bookName);
            return "Book not found";
        }
        return GenreConverter.toString(genreRepository.getByBookName(bookName));
    }

    @Transactional
    @Override
    public String addGenre(String name) {
        if (genreRepository.checkExistenceByName(name)) {
            logger.warn("Genre with name={} already exists", name);
            return "Genre already exists";
        }
        genreRepository.add(new Genre(name));
        return "Genre added";
    }

    @Transactional
    @Override
    public String deleteGenreByName(String name) {
        Genre genre;
        try {
            genre = genreRepository.getByName(name);
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with name={} not found", name);
            return "Genre not found";
        }
        if(genreRepository.checkRelationsByName(name)) {
            logger.warn("Books by genre with name={} exist", name);
            return "Books by this genre exist";
        }
        genreRepository.delete(genre);
        return "Genre deleted";
    }
}
