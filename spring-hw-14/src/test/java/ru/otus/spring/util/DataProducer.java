package ru.otus.spring.util;

import ru.otus.spring.model.mongo.MongoAuthor;
import ru.otus.spring.model.mongo.MongoBook;
import ru.otus.spring.model.mongo.MongoComment;
import ru.otus.spring.model.mongo.MongoGenre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataProducer {

    private final static String BOOK_1_NAME = "Book 1";
    private final static String BOOK_2_NAME = "Book 2";
    private final static String BOOK_3_NAME = "Book 3";
    private final static String BOOK_4_NAME = "Book 4";
    private final static MongoAuthor AUTHOR_1 = new MongoAuthor("Author 1");
    private final static MongoAuthor AUTHOR_2 = new MongoAuthor("Author 2");
    private final static MongoGenre GENRE_1 = new MongoGenre("Genre 1");
    private final static MongoGenre GENRE_2 = new MongoGenre("Genre 2");
    private final static MongoGenre GENRE_3 = new MongoGenre("Genre 3");
    private final static MongoGenre GENRE_4 = new MongoGenre("Genre 4");
    private final static MongoBook BOOK_1 = new MongoBook("1", BOOK_1_NAME, AUTHOR_1, List.of(GENRE_1, GENRE_2));
    private final static MongoBook BOOK_2 = new MongoBook("2", BOOK_2_NAME, AUTHOR_1, List.of(GENRE_2, GENRE_3));
    private final static MongoBook BOOK_3 = new MongoBook("3", BOOK_3_NAME, AUTHOR_2, List.of(GENRE_3, GENRE_4));
    private final static MongoBook BOOK_4 = new MongoBook("4", BOOK_4_NAME, AUTHOR_2, List.of(GENRE_1, GENRE_4));
    private final static MongoComment COMMENT_1 = new MongoComment("1", BOOK_1, "Comment 1 text");
    private final static MongoComment COMMENT_2 = new MongoComment("2", BOOK_1, "Comment 2 text");
    private final static MongoComment COMMENT_3 = new MongoComment("3", BOOK_2, "Comment 3 text");
    private final static MongoComment COMMENT_4 = new MongoComment("4", BOOK_2, "Comment 4 text");
    private final static MongoComment COMMENT_5 = new MongoComment("5", BOOK_3, "Comment 5 text");
    private final static MongoComment COMMENT_6 = new MongoComment("6", BOOK_3, "Comment 6 text");
    private final static MongoComment COMMENT_7 = new MongoComment("7", BOOK_4, "Comment 7 text");
    private final static MongoComment COMMENT_8 = new MongoComment("8", BOOK_4, "Comment 8 text");

    public static List<MongoAuthor> getAllAuthors() {
        return List.of(AUTHOR_1, AUTHOR_2);
    }

    public static List<MongoGenre> getAllGenres() {
        return List.of(GENRE_1, GENRE_2, GENRE_3, GENRE_4);
    }

    public static List<MongoBook> getAllBooks() {
        return List.of(BOOK_1, BOOK_2, BOOK_3, BOOK_4);
    }

    public static List<MongoComment> getAllComments() {
        return List.of(COMMENT_1, COMMENT_2, COMMENT_3, COMMENT_4, COMMENT_5, COMMENT_6, COMMENT_7, COMMENT_8);
    }

    public static Map<String, List<MongoComment>> getBookCommentsMap() {
        return Map.of(
                BOOK_1_NAME, List.of(COMMENT_1, COMMENT_2),
                BOOK_2_NAME, List.of(COMMENT_3, COMMENT_4),
                BOOK_3_NAME, List.of(COMMENT_5, COMMENT_6),
                BOOK_4_NAME, List.of(COMMENT_7, COMMENT_8)
        );
    }

    public static Optional<MongoBook> getBookById(String bookId) {
        return getAllBooks().stream()
                .filter(b -> b.getId().equals(bookId))
                .findAny();
    }

    public static Optional<MongoBook> getBookByName(String bookName) {
        return getAllBooks().stream()
                .filter(b -> b.getName().equals(bookName))
                .findAny();
    }

    public static List<MongoComment> getAllCommentsByBookId(String bookId) {
        return getAllComments().stream()
                .filter(comment -> comment.getBook().getId().equals(bookId))
                .collect(Collectors.toList());
    }

    public static Optional<MongoComment> getCommentById(String commentId) {
        return getAllComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findAny();
    }

    public static Optional<MongoAuthor> getAuthorByName(String authorName) {
        return getAllAuthors().stream()
                .filter(a -> a.getName().equals(authorName))
                .findAny();
    }

    public static List<MongoBook> getAllBooksByGenreName(String genreName) {
        return getAllBooks().stream()
                .filter(b -> b.getGenres().stream()
                        .anyMatch(g -> g.getName().equals(genreName)))
                .collect(Collectors.toList());
    }

    public static List<MongoBook> getAllBooksByAuthorName(String authorName) {
        return getAllBooks().stream()
                .filter(b -> b.getAuthor().getName().equals(authorName))
                .collect(Collectors.toList());
    }

    public static long getBooksCount() {
        return getAllBooks().size();
    }

    public static long getAuthorsCount() {
        return getAllAuthors().size();
    }

    public static long getGenresCount() {
        return getAllGenres().size();
    }
}
