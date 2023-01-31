package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorHasRelationsEx;
import ru.otus.spring.exception.AuthorNotFoundEx;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;
    private final BookDao bookDao;

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Override
    public long showAuthorsCount() {
        return authorDao.count();
    }

    @Override
    public String showAllAuthors() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Author author : authorDao.getAll()) {
            stringBuilder.append(AuthorConverter.toString(author)).append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public String showAuthorById(long id) {
        try {
            return AuthorConverter.toString(authorDao.getById(id));
        } catch (AuthorNotFoundEx e) {
            logger.warn("Author with id={} was not found", id);
            return "Author not found";
        }
    }

    @Override
    public String showAuthorByName(String name) {
        try {
            return AuthorConverter.toString(authorDao.getByName(name));
        } catch (AuthorNotFoundEx e) {
            logger.warn("Author with name={} was not found", name);
            return "Author not found";
        }
    }

    @Override
    @Transactional
    public String addAuthor(String name) {
        if (!verifyAuthorAbsence(name)) {
            return "Author already exists";
        }
        authorDao.add(new Author(0, name));
        return "Author added";
    }

    @Override
    @Transactional
    public String delAuthorByName(String name) {
        try {
            authorDao.delByName(name);
        } catch (AuthorHasRelationsEx e) {
            return "Books by this author exist";
        } catch (AuthorNotFoundEx e) {
            return "Author not found";
        }
        return "Author deleted";
    }

    private boolean verifyAuthorAbsence(String name) {
        try {
            authorDao.getByName(name);
            logger.warn("Author with name={} already exists", name);
            return false;
        } catch (AuthorNotFoundEx ignored) {
        }
        return true;
    }
}
