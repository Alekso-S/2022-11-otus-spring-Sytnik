package ru.otus.spring.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.model.jpa.JpaAuthor;
import ru.otus.spring.model.jpa.JpaBook;
import ru.otus.spring.model.jpa.JpaComment;
import ru.otus.spring.model.jpa.JpaGenre;
import ru.otus.spring.repository.jpa.JpaAuthorRepository;
import ru.otus.spring.repository.jpa.JpaBookRepository;
import ru.otus.spring.repository.jpa.JpaCommentRepository;
import ru.otus.spring.repository.jpa.JpaGenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommentWriter implements ItemWriter<CommentDto> {

    private final JpaAuthorRepository authorRepository;
    private final JpaGenreRepository genreRepository;
    private final JpaBookRepository bookRepository;
    private final JpaCommentRepository commentRepository;

    private ExecutionContext executionContext;

    @Override
    public void write(List<? extends CommentDto> commentDtos) throws JsonProcessingException {
        List<BookDto> bookDtos = new ObjectMapper().readValue(
                (String) executionContext.get("bookDtos"), new TypeReference<>() {
                });

        List<JpaAuthor> authors = bookDtos.stream()
                .map(BookDto::getAuthorName)
                .distinct()
                .map(JpaAuthor::new)
                .collect(Collectors.toList());
        authorRepository.saveAll(authors);

        List<JpaGenre> genres = bookDtos.stream()
                .flatMap(bookDto2 -> bookDto2.getGenreNames().stream())
                .distinct()
                .map(JpaGenre::new)
                .collect(Collectors.toList());
        genreRepository.saveAll(genres);

        List<JpaBook> books = bookDtos.stream()
                .map(bookDto2 -> new JpaBook(
                        bookDto2.getName(),
                        authors.stream()
                                .filter(jpaAuthor -> jpaAuthor.getName().equals(bookDto2.getAuthorName()))
                                .findAny().orElseThrow(),
                        genres.stream()
                                .filter(jpaGenre -> bookDto2.getGenreNames().contains(jpaGenre.getName()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        bookRepository.saveAll(books);

        List<JpaComment> comments = commentDtos.stream()
                .map(commentDto -> new JpaComment(
                        commentDto.getText(),
                        books.stream()
                                .filter(book -> book.getName().equals(commentDto.getBookName()))
                                .findAny().orElseThrow()))
                .collect(Collectors.toList());
        commentRepository.saveAll(comments);
    }

    @BeforeStep
    public void getJobExecutionContext(StepExecution stepExecution) {
        this.executionContext = stepExecution.getJobExecution().getExecutionContext();
    }
}
