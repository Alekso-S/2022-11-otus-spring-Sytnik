package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.model.jpa.JpaAuthor;
import ru.otus.spring.model.jpa.JpaBook;
import ru.otus.spring.model.jpa.JpaComment;
import ru.otus.spring.model.jpa.JpaGenre;
import ru.otus.spring.model.mongo.MongoBook;
import ru.otus.spring.model.mongo.MongoComment;
import ru.otus.spring.repository.jpa.JpaAuthorRepository;
import ru.otus.spring.repository.jpa.JpaBookRepository;
import ru.otus.spring.repository.jpa.JpaCommentRepository;
import ru.otus.spring.repository.jpa.JpaGenreRepository;
import ru.otus.spring.util.DataProducer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(JpaServiceImpl.class)
@DisplayName("Сервис JPA должен")
class JpaServiceImplTest {

    @Autowired
    private JpaService service;

    @MockBean
    private JpaAuthorRepository authorRepository;
    @MockBean
    private JpaGenreRepository genreRepository;
    @MockBean
    private JpaBookRepository bookRepository;
    @MockBean
    private JpaCommentRepository commentRepository;

    private static final String MONGO_BOOK_1_ID = "1";
    private static final Long JPA_AUTHOR_1_ID = 1L;
    private static final Long JPA_GENRE_1_ID = 1L;
    private static final String MONGO_COMMENT_1_ID = "1";
    private static final Long JPA_BOOK_1_ID = 1L;

    @Test
    @DisplayName("очищать БД")
    void erase() {
        service.erase();

        verify(commentRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("готовить актуальную сущность книги для записи")
    void prepareJpaBook() {
        MongoBook mongoBook = DataProducer.getBookById(MONGO_BOOK_1_ID).orElseThrow();
        when(authorRepository.findByName(mongoBook.getAuthor().getName()))
                .thenReturn(Optional.of(new JpaAuthor(JPA_AUTHOR_1_ID, mongoBook.getAuthor().getName())));
        when(genreRepository.findByName(mongoBook.getGenres().get(0).getName()))
                .thenReturn(Optional.of(new JpaGenre(JPA_GENRE_1_ID, mongoBook.getGenres().get(0).getName())));

        JpaBook jpaBook = service.prepareJpaBook(mongoBook);

        assertEquals(new BookDto(jpaBook), new BookDto(mongoBook));
        assertEquals(jpaBook.getAuthor().getId(), JPA_AUTHOR_1_ID);
        assertEquals(jpaBook.getGenres().get(0).getId(), JPA_GENRE_1_ID);
    }

    @Test
    @DisplayName("готовить актуальную сущность комментария для записи")
    void prepareJpaComment() {
        MongoComment mongoComment = DataProducer.getCommentById(MONGO_COMMENT_1_ID).orElseThrow();
        MongoBook mongoBook = mongoComment.getBook();
        when(bookRepository.findByName(mongoBook.getName()))
                .thenReturn(Optional.of(new JpaBook(JPA_BOOK_1_ID, mongoBook.getName(), null, null)));

        JpaComment jpaComment = service.prepareJpaComment(mongoComment);

        assertEquals(new CommentDto(jpaComment), new CommentDto(mongoComment));
        assertEquals(jpaComment.getBook().getId(), JPA_BOOK_1_ID);
    }
}