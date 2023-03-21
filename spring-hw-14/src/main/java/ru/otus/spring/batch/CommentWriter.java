package ru.otus.spring.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.model.jpa.JpaBook;
import ru.otus.spring.model.jpa.JpaComment;
import ru.otus.spring.repository.jpa.JpaBookRepository;
import ru.otus.spring.repository.jpa.JpaCommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommentWriter implements ItemWriter<CommentDto> {

    private final JpaBookRepository bookRepository;
    private final JpaCommentRepository commentRepository;

    @Override
    public void write(List<? extends CommentDto> commentDtos) {
        List<JpaBook> books = bookRepository.findAll();

        List<JpaComment> comments = commentDtos.stream()
                .map(commentDto -> new JpaComment(
                        commentDto.getText(),
                        books.stream()
                                .filter(book -> book.getName().equals(commentDto.getBookName()))
                                .findAny().orElseThrow()))
                .collect(Collectors.toList());

        commentRepository.saveAll(comments);
    }
}
