package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.AuthorNotFoundEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.GenreNotFoundEx;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public long showBooksCount() {
        return bookDao.count();
    }

    @Override
    public String showAllBooks() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book : bookDao.getAll()) {
            fillBookInfo(book);
            stringBuilder.append(BookConverter.toString(book)).append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public String showBookById(long id) {
        Book book = getBookById(id);
        if (book == null) {
            return "Book not found";
        }
        fillBookInfo(book);
        return BookConverter.toString(book);
    }

    @Override
    public String showBookByName(String name) {
        Book book = getBookByName(name);
        if (book == null) {
            return "Book not found";
        }
        fillBookInfo(book);
        return BookConverter.toString(book);
    }

    @Override
    public String showBooksByGenreId(Long genreId) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book : bookDao.getByGenreId(genreId)) {
            fillBookInfo(book);
            stringBuilder.append(BookConverter.toString(book)).append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public String showBooksByGenreName(String genreName) {
        Genre genre = getGenreByName(genreName);
        if (genre == null) {
            return "Genre not found";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book : bookDao.getByGenreId(genre.getId())) {
            fillBookInfo(book);
            stringBuilder.append(BookConverter.toString(book)).append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    @Transactional
    public String addBook(String name, String authorName, String... genreNames) {
        if (!verifyBookAbsence(name)) {
            return "Book already exists";
        }
        Author author = prepareAuthor(authorName);
        Book book = bookDao.add(new Book(0, author.getId(), name));
        addGenres(book, genreNames);
        return "Book added";
    }

    @Override
    @Transactional
    public String delBookByName(String name) {
        Book book = getBookByName(name);
        if (book == null) {
            return "Book not found";
        }
        genreDao.delGenresForBook(book.getId());
        bookDao.delByName(book.getName());
        return "Book deleted";
    }

    private Book getBookByName(String name) {
        try {
            return bookDao.getByName(name);
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} was not found", name);
            return null;
        }
    }

    private void addGenres(Book book, String[] genreNames) {
        Genre genre;
        for (String genreName : genreNames) {
            try {
                genre = genreDao.getByName(genreName);
            } catch (GenreNotFoundEx e) {
                genre = genreDao.add(new Genre(0, genreName));
            }
            genreDao.addGenreForBook(book.getId(), genre.getId());
        }
    }

    private Author prepareAuthor(String authorName) {
        try {
            return authorDao.getByName(authorName);
        } catch (AuthorNotFoundEx e) {
            return authorDao.add(new Author(0, authorName));
        }
    }

    private boolean verifyBookAbsence(String name) {
        try {
            bookDao.getByName(name);
            logger.warn("Book with name={} already exists", name);
            return false;
        } catch (BookNotFoundEx ignored) {
        }
        return true;
    }

    private void fillBookInfo(Book book) {
        try {
            book.setAuthor(authorDao.getById(book.getAuthorId()));
        } catch (AuthorNotFoundEx e) {
            book.setAuthor(new Author(0, "Missing author"));
        }
        book.setGenres(genreDao.getByBookId(book.getId()));
    }

    private Book getBookById(long id) {
        try {
            return bookDao.getById(id);
        } catch (BookNotFoundEx e) {
            logger.warn("Book with id={} was not found", id);
            return null;
        }
    }

    private Genre getGenreByName(String genreName) {
        try {
            return genreDao.getByName(genreName);
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with name={} was not found", genreName);
            return null;
        }
    }
}
