package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.CommentConverter;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.exception.BookNotFoundEx;
import ru.otus.spring.exception.CommentNotFoundEx;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final BookRepository bookRepository;

    @Override
    public CommentDto getById(String id) throws CommentNotFoundEx {
        return repository.findById(id)
                .orElseThrow(() -> new CommentNotFoundEx(id))
                .toDtoWithBookName();
    }

    @Override
    public List<CommentDto> getAllByBookName(String bookName) throws BookNotFoundEx {
        if (!bookRepository.existsByName(bookName)) {
            throw new BookNotFoundEx(bookName);
        }
        return CommentConverter.toDto(repository.findAllByBookName(bookName));
    }

    @Override
    public CommentDto addByBookName(String bookName, String text) throws BookNotFoundEx {
        Book book = bookRepository.findByName(bookName)
                .orElseThrow(() -> new BookNotFoundEx(bookName));
        return repository.save(new Comment(book, text)).toDto();
    }

    @Override
    public CommentDto updateById(String id, String text) throws CommentNotFoundEx {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new CommentNotFoundEx(id));
        comment.setText(text);
        repository.save(comment);
        return comment.toDtoWithBookName();
    }

    @Override
    public CommentDto deleteById(String id) throws CommentNotFoundEx {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new CommentNotFoundEx(id));
        repository.delete(comment);
        return comment.toDtoWithBookName();
    }
}
