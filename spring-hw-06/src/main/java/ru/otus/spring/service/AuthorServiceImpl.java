package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorHasRelationsEx;
import ru.otus.spring.exception.AuthorNotFoundEx;
import ru.otus.spring.repository.AuthorRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Override
    public long showAuthorsCount() {
        return authorRepository.count();
    }

    @Override
    public String showAuthorById(long id) {
        try {
            return AuthorConverter.toString(authorRepository.getById(id));
        } catch (AuthorNotFoundEx e) {
            logger.warn("Author with id={} was not found", id);
            return "Author not found";
        }
    }

    @Override
    public String showAuthorByName(String name) {
        try {
            return AuthorConverter.toString(authorRepository.getByName(name));
        } catch (AuthorNotFoundEx e) {
            logger.warn("Author with name={} was not found", name);
            return "Author not found";
        }
    }

    @Override
    public String showAllAuthors() {
        return AuthorConverter.toString(authorRepository.getAll());
    }

    @Transactional
    @Override
    public String addAuthor(String name) {
        if (authorRepository.checkExistenceByName(name)) {
            logger.warn("Author with name={} already exists", name);
            return "Author already exists";
        }
        authorRepository.add(new Author(name));
        return "Author added";
    }

    @Override
    @Transactional
    public String delAuthorByName(String name) {
        try {
            authorRepository.delByName(name);
        } catch (AuthorHasRelationsEx e) {
            logger.warn("Books by author with name={} exist", name);
            return "Books by this author exist";
        } catch (AuthorNotFoundEx e) {
            logger.warn("Author with name={} not found", name);
            return "Author not found";
        }
        return "Author deleted";
    }
}
