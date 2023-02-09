package ru.otus.spring.util;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.BookNotFoundEx;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataProducer {

    private final static Author AUTHOR_1 = new Author(1L, "Author 1");
    private final static Author AUTHOR_2 = new Author(2L, "Author 2");
    private final static Genre GENRE_1 = new Genre(1L, "Genre 1");
    private final static Genre GENRE_2 = new Genre(2L, "Genre 2");
    private final static Genre GENRE_3 = new Genre(3L, "Genre 3");
    private final static Genre GENRE_4 = new Genre(4L, "Genre 4");
    private final static Comment COMMENT_1 = new Comment(1L, "Comment 1 text");
    private final static Comment COMMENT_2 = new Comment(2L, "Comment 2 text");
    private final static Comment COMMENT_3 = new Comment(3L, "Comment 3 text");
    private final static Comment COMMENT_4 = new Comment(4L, "Comment 4 text");
    private final static Comment COMMENT_5 = new Comment(5L, "Comment 5 text");
    private final static Comment COMMENT_6 = new Comment(6L, "Comment 6 text");
    private final static Comment COMMENT_7 = new Comment(7L, "Comment 7 text");
    private final static Comment COMMENT_8 = new Comment(8L, "Comment 8 text");
    private final static Book BOOK_1 = new Book(1L, "Book 1", AUTHOR_1,
            List.of(GENRE_1, GENRE_2), List.of(COMMENT_1,COMMENT_2));
    private final static Book BOOK_2 = new Book(2L, "Book 2", AUTHOR_1,
            List.of(GENRE_2, GENRE_3), List.of(COMMENT_3,COMMENT_4));
    private final static Book BOOK_3 = new Book(3L, "Book 3", AUTHOR_2,
            List.of(GENRE_3, GENRE_4), List.of(COMMENT_5,COMMENT_6));
    private final static Book BOOK_4 = new Book(4L, "Book 4", AUTHOR_2,
            List.of(GENRE_1, GENRE_4), List.of(COMMENT_7,COMMENT_8));

    public static List<Author> getAllAuthors() {
        return List.of(AUTHOR_1, AUTHOR_2);
    }

    public static List<Genre> getAllGenres() {
        return List.of(GENRE_1, GENRE_2, GENRE_3, GENRE_4);
    }

    public static List<Book> getAllBooks() {
        return List.of(BOOK_1, BOOK_2, BOOK_3, BOOK_4);
    }

    private static List<Comment> getAllComments() {
        return List.of(COMMENT_1,COMMENT_2,COMMENT_3,COMMENT_4,COMMENT_5,COMMENT_6,COMMENT_7,COMMENT_8);
    }

    public static List<Genre> getAllGenresByBookId(long bookId) {
        return getAllBooks().stream()
                .filter(b -> b.getId() == bookId)
                .findAny().orElseThrow().getGenres();
    }

    public static Optional<Book> getBookById(long bookId) {
        return getAllBooks().stream()
                .filter(b -> b.getId() == bookId)
                .findAny();
    }

    public static Optional<Book> getBookByName(String bookName) {
        return getAllBooks().stream()
                .filter(b -> b.getName().equals(bookName))
                .findAny();
    }

    public static List<Book> getAllBooksByGenreId(long genreId) {
        return getAllBooks()
                .stream()
                .filter((b) -> b.getGenres().stream()
                        .anyMatch(g -> g.getId() == genreId))
                .collect(Collectors.toList());
    }

    public static List<Comment> getAllCommentsByBookId(long bookId) {
        return getBookById(bookId).orElseThrow().getComments();
    }

    public static List<Comment> getAllCommentsByBookName(String bookName) throws BookNotFoundEx {
        return getBookByName(bookName).orElseThrow(BookNotFoundEx::new).getComments();
    }

    public static Optional<Comment> getCommentById(long commentId) {
        return getAllComments().stream()
                .filter(c -> c.getId() == commentId)
                .findAny();
    }

    public static Optional<Author> getAuthorByName(String authorName) {
        return getAllAuthors().stream()
                .filter(a -> a.getName().equals(authorName))
                .findAny();
    }

    public static Optional<Genre> getGenreByName(String genreName) {
        return getAllGenres().stream()
                .filter(g -> g.getName().equals(genreName))
                .findAny();
    }

    public static List<Book> getAllBooksByGenreName(String genreName) {
        return getAllBooks().stream()
                .filter(b -> b.getGenres().stream()
                        .anyMatch(g -> g.getName().equals(genreName)))
                .collect(Collectors.toList());
    }

    public static Optional<Author> getAuthorById(long authorId) {
        return getAllAuthors().stream()
                .filter(a -> a.getId() == authorId)
                .findAny();
    }
}
