package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorNotFoundEx;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Override
    public long showCount() {
        return authorRepository.count();
    }

    @Override
    public String showById(long id) {
        try {
            return AuthorConverter.toString(authorRepository.findById(id).orElseThrow(AuthorNotFoundEx::new));
        } catch (AuthorNotFoundEx e) {
            logger.warn("Author with id={} was not found", id);
            return "Author not found";
        }
    }

    @Override
    public String showByName(String name) {
        try {
            return AuthorConverter.toString(authorRepository.findByName(name).orElseThrow(AuthorNotFoundEx::new));
        } catch (AuthorNotFoundEx e) {
            logger.warn("Author with name={} was not found", name);
            return "Author not found";
        }
    }

    @Override
    public String showAll() {
        return AuthorConverter.toString(authorRepository.findAll());
    }

    @Transactional
    @Override
    public String add(String name) {
        if (authorRepository.existsByName(name)) {
            logger.warn("Author with name={} already exists", name);
            return "Author already exists";
        }
        authorRepository.save(new Author(name));
        return "Author added";
    }

    @Override
    @Transactional
    public String deleteByName(String name) {
        Author author;
        try {
            author = authorRepository.findByName(name).orElseThrow(AuthorNotFoundEx::new);
        } catch (AuthorNotFoundEx e) {
            logger.warn("Author with name={} not found", name);
            return "Author not found";
        }
        if (bookRepository.existsByAuthorName(name)) {
            logger.warn("Books by author with name={} exist", name);
            return "Books by this author exist";
        }
        authorRepository.delete(author);
        return "Author deleted";
    }
}
