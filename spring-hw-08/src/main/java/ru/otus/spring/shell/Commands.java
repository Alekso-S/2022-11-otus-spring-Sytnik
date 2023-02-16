package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.GenreService;

@SuppressWarnings("SpellCheckingInspection")
@RequiredArgsConstructor
@ShellComponent
public class Commands {

    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookService bookService;
    private final CommentService commentService;

    //Authors
    @ShellMethod(key = {"authors-count", "ac"}, value = "Show authors count")
    public long getAuthorsCount() {
        return authorService.showCount();
    }

    @ShellMethod(key = {"authors-get", "ag"}, value = "Show all authors")
    public String getAllAuthors() {
        return authorService.showAll();
    }

    //Genres
    @ShellMethod(key = {"genres-count", "gc"}, value = "Show genres count")
    public long getGenresCount() {
        return genreService.showCount();
    }

    @ShellMethod(key = {"genres-get", "gg"}, value = "Show all genres")
    public String getAllGenres() {
        return genreService.showAll();
    }

    @ShellMethod(key = {"genres-get-book-id", "ggbi"}, value = "Show genres by book id")
    public String getGenresByBookId(String id) {
        return genreService.showAllByBookId(id);
    }

    @ShellMethod(key = {"genres-get-book-name", "ggbn"}, value = "Show genres by book name")
    public String getGenresByBookName(String bookName) {
        return genreService.showAllByBookName(bookName);
    }

    //Books
    @ShellMethod(key = {"books-count", "bc"}, value = "Show books count")
    public long getBooksCount() {
        return bookService.showCount();
    }

    @ShellMethod(key = {"books-get", "bg"}, value = "Show all books")
    public String getAllBooks() {
        return bookService.showAll();
    }

    @ShellMethod(key = {"book-get-id", "bgi"}, value = "Show book by id")
    public String getBookById(String id) {
        return bookService.showById(id);
    }

    @ShellMethod(key = {"book-get-name", "bgn"}, value = "Show book by name")
    public String getBookByName(String name) {
        return bookService.showByName(name);
    }

    @ShellMethod(key = {"book-get-genre-name", "bggn"}, value = "Show books by genre name")
    public String getBooksByGenreName(String genreName) {
        return bookService.showAllByGenreName(genreName);
    }

    @ShellMethod(key = {"book-add", "ba"}, value = "Add new book")
    public String addBook(String bookName, String authorName, String... genreNames) {
        return bookService.add(bookName, authorName, genreNames);
    }

    @ShellMethod(key = {"book-del-name", "bdn"}, value = "Remove book by name")
    public String delBookByName(String name) {
        return bookService.deleteByName(name);
    }

    //Comments
    @ShellMethod(key = {"comment-count-book-id", "ccbi"}, value = "Show comments count by book id")
    public String getCommentsCountByBookId(String bookId) {
        return commentService.showCountByBookId(bookId);
    }

    @ShellMethod(key = {"comment-count-book-name", "ccbn"}, value = "Show comments count by book name")
    public String getCommentsCountByBookName(String bookName) {
        return commentService.showCountByBookName(bookName);
    }

    @ShellMethod(key = {"comment-get-id", "cgi"}, value = "Show comment by id")
    public String getCommentById(String id) {
        return commentService.showById(id);
    }

    @ShellMethod(key = {"comment-get-book-id", "cgbi"}, value = "Show comments by book id")
    public String getCommentsByBookId(String bookId) {
        return commentService.showAllByBookId(bookId);
    }

    @ShellMethod(key = {"comment-get-book-name", "cgbn"}, value = "Show comments by book name")
    public String getCommentsByBookName(String bookName) {
        return commentService.showAllByBookName(bookName);
    }

    @ShellMethod(key = {"comment-add-book-name", "cabn"}, value = "Add comment by book name")
    public String addCommentByBookName(String bookName, String text) {
        return commentService.addByBookName(bookName, text);
    }
    @ShellMethod(key = {"comment-upd-id", "cui"}, value = "Update comment by id")
    public String updCommentById(String id, String text) {
        return commentService.updateById(id, text);
    }

    @ShellMethod(key = {"comment-del-id", "cdi"}, value = "Delete comment by id")
    public String delCommentById(String id) {
        return commentService.deleteById(id);
    }
}
