package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.exception.BookAlreadyExistsEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.repository.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<BookDto> getAll() {
        return BookConverter.toDto(repository.findAll());
    }

    @Override
    public List<BookDto> getAllByGenreName(String genreName) {
        return BookConverter.toDto(repository.findAllByGenresName(genreName));
    }

    @Override
    public List<BookDto> getAllByAuthorName(String authorName) {
        return BookConverter.toDto(repository.findAllByAuthorName(authorName));
    }

    @Override
    public BookDto add(String name, String authorName, String genreNames) throws BookAlreadyExistsEx {
        if (repository.existsByName(name)) {
            throw new BookAlreadyExistsEx(name);
        }
        return repository.save(new Book(name, authorName, genreNames)).toDto();
    }

    @Override
    public void deleteByName(String name) throws BookNotFoundEx {
        Book book = repository.findByName(name).orElseThrow(() -> new BookNotFoundEx(name));
        repository.deleteWithComments(book);
    }
}
