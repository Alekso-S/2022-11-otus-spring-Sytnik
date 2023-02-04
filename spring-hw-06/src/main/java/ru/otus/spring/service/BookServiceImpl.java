package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.AuthorNotFoundEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.GenreNotFoundEx;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public long showBooksCount() {
        return bookRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public String showBookById(long id) {
        try {
            return BookConverter.toString(bookRepository.getById(id));
        } catch (BookNotFoundEx e) {
            logger.warn("Book with id={} was not found", id);
            return "Book not found";
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String showBookByName(String name) {
        try {
            return BookConverter.toString(bookRepository.getByName(name));
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} was not found", name);
            return "Book not found";
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String showAllBooks() {
        return BookConverter.toString(bookRepository.getAll());
    }

    @Transactional(readOnly = true)
    @Override
    public String showBooksByGenreId(long genreId) {
        if (!genreRepository.checkExistenceById(genreId)) {
            logger.warn("Genre with id={} was not found", genreId);
            return "Genre not found";
        }
        return BookConverter.toString(bookRepository.getByGenreId(genreId));
    }

    @Transactional(readOnly = true)
    @Override
    public String showBooksByGenreName(String genreName) {
        if (!genreRepository.checkExistenceByName(genreName)) {
            logger.warn("Genre with name={} was not found", genreName);
            return "Genre not found";
        }
        return BookConverter.toString(bookRepository.getByGenreName(genreName));
    }

    @Transactional
    @Override
    public String addBook(String name, String authorName, String... genreNames) {
        if (bookRepository.checkExistenceByName(name)) {
            logger.warn("Book with name={} already exists", name);
            return "Book already exists";
        }
        Author author = prepareAuthor(authorName);
        List<Genre> genres = prepareGenres(genreNames);
        bookRepository.add(new Book(name, author, genres));
        return "Book added";
    }

    @Transactional
    @Override
    public String delBookByName(String name) {
        try {
            bookRepository.delByName(name);
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} not found", name);
            return "Book not found";
        }
        return "Book deleted";
    }

    private Author prepareAuthor(String authorName) {
        try {
            return authorRepository.getByName(authorName);
        } catch (AuthorNotFoundEx e) {
            return authorRepository.add(new Author(authorName));
        }
    }

    private List<Genre> prepareGenres(String[] genreNames) {
        List<Genre> genres = new ArrayList<>();
        for (String genreName : genreNames) {
            try {
                genres.add(genreRepository.getByName(genreName));
            } catch (GenreNotFoundEx e) {
                genres.add(genreRepository.add(new Genre(genreName)));
            }
        }
        return genres;
    }
}
