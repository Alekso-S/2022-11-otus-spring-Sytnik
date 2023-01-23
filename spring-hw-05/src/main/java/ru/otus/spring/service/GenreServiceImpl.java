package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.converter.GenreConverter;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.GenreNotFoundEx;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;
    private final BookDao bookDao;

    private static final Logger logger = LoggerFactory.getLogger(GenreServiceImpl.class);

    @Override
    public long showGenresCount() {
        return genreDao.count();
    }

    @Override
    public String showAllGenres() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : genreDao.getAll()) {
            stringBuilder.append(GenreConverter.toString(genre)).append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public String showGenreById(long id) {
        try {
            return GenreConverter.toString(genreDao.getById(id));
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with id={} was not found", id);
            return "Genre not found";
        }
    }

    @Override
    public String showGenreByName(String name) {
        try {
            return GenreConverter.toString(genreDao.getByName(name));
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with name={} was not found", name);
            return "Genre not found";
        }
    }

    @Override
    public String showGenresByBookId(long bookId) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : genreDao.getByBookId(bookId)) {
            stringBuilder.append(GenreConverter.toString(genre)).append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public String showGenresByBookName(String bookName) {
        Book book;
        try {
            book = bookDao.getByName(bookName);
        } catch (BookNotFoundEx e) {
            logger.warn("Book with name={} was not found", bookName);
            return "Book not found";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : genreDao.getByBookId(book.getId())) {
            stringBuilder.append(GenreConverter.toString(genre)).append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    @Transactional
    public String addGenre(String name) {
        if (!verifyGenreAbsence(name)) {
            return "Genre already exists";
        }
        genreDao.add(new Genre(0, name));
        return "Genre added";
    }

    @Override
    @Transactional
    public String delGenreByName(String name) {
        Genre genre = getGenreByName(name);
        if (genre == null) {
            return "Genre not found";
        }
        if (bookDao.getByGenreId(genre.getId()).size() == 0) {
            genreDao.delByName(name);
        } else {
            return "Books of this genre exists";
        }
        return "Genre deleted";
    }

    private Genre getGenreByName(String name) {
        try {
            return genreDao.getByName(name);
        } catch (GenreNotFoundEx e) {
            logger.warn("Genre with name={} was not found", name);
            return null;
        }
    }

    private boolean verifyGenreAbsence(String name) {
        try {
            genreDao.getByName(name);
            logger.warn("Genre with name={} already exists", name);
            return false;
        } catch (GenreNotFoundEx ignored) {
        }
        return true;
    }
}
