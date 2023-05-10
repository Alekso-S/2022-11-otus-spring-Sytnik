package ru.otus.spring.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.BookConverter;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.exception.BookAlreadyExistsEx;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.repository.BookRepository;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @HystrixCommand(defaultFallback = "countFallback")
    public long count() {
        return bookRepository.count();
    }

    private long countFallback() {
        return -1;
    }

    @Override
    @HystrixCommand(defaultFallback = "getListFallback")
    public List<BookDto> getAll() {
        return BookConverter.toDto(bookRepository.findAll());
    }

    @Override
    @HystrixCommand(defaultFallback = "getListFallback")
    public List<BookDto> getAllByGenreName(String genreName) {
        return BookConverter.toDto(bookRepository.findAllByGenresName(genreName));
    }

    @Override
    @HystrixCommand(defaultFallback = "getListFallback")
    public List<BookDto> getAllByAuthorName(String authorName) {
        return BookConverter.toDto(bookRepository.findAllByAuthorName(authorName));
    }

    private List<BookDto> getListFallback() {
        return List.of(new Book("Error", "Error", "Error").toDto());
    }

    @Override
    public BookDto getById(String bookId) throws BookNotFoundEx {
        return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundEx("id", bookId)).toDto();
    }

    @Override
    public BookDto add(String name, String authorName, String genreNames) throws BookAlreadyExistsEx {
        if (bookRepository.existsByName(name)) {
            throw new BookAlreadyExistsEx(name);
        }
        return bookRepository.save(new Book(name, authorName, genreNames)).toDto();
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BookNotFoundEx {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundEx("id", id));
        bookRepository.deleteWithComments(book);
    }
}
