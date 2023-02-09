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
import ru.otus.spring.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    @Override
    public long showCount() {
        return bookRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public String showById(long id) {
        try {
            return BookConverter.toString(bookRepository.findById(id).orElseThrow(BookNotFoundEx::new));
        } catch (BookNotFoundEx e) {
            logger.warn("Book with id={} was not found", id);
            return "Book not found";
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String showByName(String name) {
        try {
            return BookConverter.toString(bookRepository.findByName(name).orElseThrow(BookNotFoundEx::new));
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} was not found", name);
            return "Book not found";
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String showAll() {
        return BookConverter.toString(bookRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public String showAllByGenreId(long genreId) {
        if (!genreRepository.existsById(genreId)) {
            logger.warn("Genre with id={} was not found", genreId);
            return "Genre not found";
        }
        return BookConverter.toString(bookRepository.findAllByGenresId(genreId));
    }

    @Transactional(readOnly = true)
    @Override
    public String showAllByGenreName(String genreName) {
        if (!genreRepository.existsByName(genreName)) {
            logger.warn("Genre with name={} was not found", genreName);
            return "Genre not found";
        }
        return BookConverter.toString(bookRepository.findAllByGenresName(genreName));
    }

    @Transactional
    @Override
    public String add(String name, String authorName, String... genreNames) {
        if (bookRepository.existsByName(name)) {
            logger.warn("Book with name={} already exists", name);
            return "Book already exists";
        }
        Author author = prepareAuthor(authorName);
        List<Genre> genres = prepareGenres(genreNames);
        bookRepository.save(new Book(name, author, genres));
        return "Book added";
    }

    @Transactional
    @Override
    public String deleteByName(String name) {
        Book book;
        try {
            book = bookRepository.findByName(name).orElseThrow(BookNotFoundEx::new);
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} not found", name);
            return "Book not found";
        }
        commentRepository.deleteByBookName(name);
        bookRepository.delete(book);
        return "Book deleted";
    }

    private Author prepareAuthor(String authorName) {
        try {
            return authorRepository.findByName(authorName).orElseThrow(AuthorNotFoundEx::new);
        } catch (AuthorNotFoundEx e) {
            return authorRepository.save(new Author(authorName));
        }
    }

    private List<Genre> prepareGenres(String[] genreNames) {
        List<Genre> genres = new ArrayList<>();
        for (String genreName : genreNames) {
            try {
                genres.add(genreRepository.findByName(genreName).orElseThrow(GenreNotFoundEx::new));
            } catch (GenreNotFoundEx e) {
                genres.add(genreRepository.save(new Genre(genreName)));
            }
        }
        return genres;
    }
}
