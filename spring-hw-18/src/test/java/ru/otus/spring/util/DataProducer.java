package ru.otus.spring.util;

import ru.otus.spring.domain.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataProducer {

    private final static String BOOK_1_NAME = "Book 1";
    private final static String BOOK_2_NAME = "Book 2";
    private final static String BOOK_3_NAME = "Book 3";
    private final static String BOOK_4_NAME = "Book 4";
    private final static Author AUTHOR_1 = new Author("Author 1");
    private final static Author AUTHOR_2 = new Author("Author 2");
    private final static Genre GENRE_1 = new Genre("Genre 1");
    private final static Genre GENRE_2 = new Genre("Genre 2");
    private final static Genre GENRE_3 = new Genre("Genre 3");
    private final static Genre GENRE_4 = new Genre("Genre 4");
    private final static Book BOOK_1 = new Book("1", BOOK_1_NAME, AUTHOR_1, List.of(GENRE_1, GENRE_2));
    private final static Book BOOK_2 = new Book("2", BOOK_2_NAME, AUTHOR_1, List.of(GENRE_2, GENRE_3));
    private final static Book BOOK_3 = new Book("3", BOOK_3_NAME, AUTHOR_2, List.of(GENRE_3, GENRE_4));
    private final static Book BOOK_4 = new Book("4", BOOK_4_NAME, AUTHOR_2, List.of(GENRE_1, GENRE_4));
    private final static UserAccount ADMIN = new UserAccount("1", "admin", "pwd", List.of("ADMIN"));
    private final static UserAccount USER_1 = new UserAccount("2", "user1", "pwd", List.of("USER"));
    private final static UserAccount USER_2 = new UserAccount("3", "user2", "pwd", List.of("USER"));
    private final static Comment COMMENT_1 = new Comment("1", BOOK_1, "Comment 1 text", USER_1);
    private final static Comment COMMENT_2 = new Comment("2", BOOK_1, "Comment 2 text", USER_2);
    private final static Comment COMMENT_3 = new Comment("3", BOOK_2, "Comment 3 text", USER_1);
    private final static Comment COMMENT_4 = new Comment("4", BOOK_2, "Comment 4 text", USER_2);
    private final static Comment COMMENT_5 = new Comment("5", BOOK_3, "Comment 5 text", USER_1);
    private final static Comment COMMENT_6 = new Comment("6", BOOK_3, "Comment 6 text", USER_2);
    private final static Comment COMMENT_7 = new Comment("7", BOOK_4, "Comment 7 text", USER_1);
    private final static Comment COMMENT_8 = new Comment("8", BOOK_4, "Comment 8 text", USER_2);

    public static List<Author> getAllAuthors() {
        return List.of(AUTHOR_1, AUTHOR_2);
    }

    public static List<Genre> getAllGenres() {
        return List.of(GENRE_1, GENRE_2, GENRE_3, GENRE_4);
    }

    public static List<Book> getAllBooks() {
        return List.of(BOOK_1, BOOK_2, BOOK_3, BOOK_4);
    }

    public static List<Comment> getAllComments() {
        return List.of(COMMENT_1, COMMENT_2, COMMENT_3, COMMENT_4, COMMENT_5, COMMENT_6, COMMENT_7, COMMENT_8);
    }

    public static List<UserAccount> getAllUsers() {
        return List.of(ADMIN, USER_1, USER_2);
    }

    public static Optional<UserAccount> getUserByName(String name) {
        return getAllUsers().stream()
                .filter(userAccount -> userAccount.getUsername().equals(name))
                .findAny();
    }

    public static Map<String, List<Comment>> getBookCommentsMap() {
        return Map.of(
                BOOK_1_NAME, List.of(COMMENT_1, COMMENT_2),
                BOOK_2_NAME, List.of(COMMENT_3, COMMENT_4),
                BOOK_3_NAME, List.of(COMMENT_5, COMMENT_6),
                BOOK_4_NAME, List.of(COMMENT_7, COMMENT_8)
        );
    }

    public static Optional<Book> getBookById(String bookId) {
        return getAllBooks().stream()
                .filter(b -> b.getId().equals(bookId))
                .findAny();
    }

    public static Optional<Book> getBookByName(String bookName) {
        return getAllBooks().stream()
                .filter(b -> b.getName().equals(bookName))
                .findAny();
    }

    public static List<Comment> getAllCommentsByBookId(String bookId) {
        return getAllComments().stream()
                .filter(comment -> comment.getBook().getId().equals(bookId))
                .collect(Collectors.toList());
    }

    public static Optional<Comment> getCommentById(String commentId) {
        return getAllComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findAny();
    }

    public static Optional<Author> getAuthorByName(String authorName) {
        return getAllAuthors().stream()
                .filter(a -> a.getName().equals(authorName))
                .findAny();
    }

    public static List<Book> getAllBooksByGenreName(String genreName) {
        return getAllBooks().stream()
                .filter(b -> b.getGenres().stream()
                        .anyMatch(g -> g.getName().equals(genreName)))
                .collect(Collectors.toList());
    }

    public static List<Book> getAllBooksByAuthorName(String authorName) {
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
