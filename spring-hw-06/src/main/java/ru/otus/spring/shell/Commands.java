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
        return authorService.showAuthorsCount();
    }

    @ShellMethod(key = {"authors-get", "ag"}, value = "Show all authors")
    public String getAllAuthors() {
        return authorService.showAllAuthors();
    }

    @ShellMethod(key = {"author-get-id", "agi"}, value = "Show author by id")
    public String getAuthorById(long id) {
        return authorService.showAuthorById(id);
    }

    @ShellMethod(key = {"author-get-name", "agn"}, value = "Show author by name")
    public String getAuthorByName(String name) {
        return authorService.showAuthorByName(name);
    }

    @ShellMethod(key = {"author-add", "aa"}, value = "Add new author")
    public String addAuthor(String name) {
        return authorService.addAuthor(name);
    }

    @ShellMethod(key = {"author-del-name", "adn"}, value = "Remove author by name")
    public String delAuthorByName(String name) {
        return authorService.deleteAuthorByName(name);
    }

    //Genres
    @ShellMethod(key = {"genres-count", "gc"}, value = "Show genres count")
    public long getGenresCount() {
        return genreService.showGenresCount();
    }

    @ShellMethod(key = {"genres-get", "gg"}, value = "Show all genres")
    public String getAllGenres() {
        return genreService.showAllGenres();
    }

    @ShellMethod(key = {"genre-get-id", "ggi"}, value = "Show genre by id")
    public String getGenreById(long id) {
        return genreService.showGenreById(id);
    }

    @ShellMethod(key = {"genre-get-name", "ggn"}, value = "Show genre by name")
    public String getGenreByName(String name) {
        return genreService.showGenreByName(name);
    }

    @ShellMethod(key = {"genres-get-book-id", "ggbi"}, value = "Show genres by book id")
    public String getGenresByBookId(long id) {
        return genreService.showGenresByBookId(id);
    }

    @ShellMethod(key = {"genres-get-book-name", "ggbn"}, value = "Show genres by book name")
    public String getGenresByBookName(String bookName) {
        return genreService.showGenresByBookName(bookName);
    }

    @ShellMethod(key = {"genre-add", "ga"}, value = "Add new genre")
    public String addGenre(String name) {
        return genreService.addGenre(name);
    }

    @ShellMethod(key = {"genre-del-name", "gdn"}, value = "Remove genre by name")
    public String delGenreByName(String name) {
        return genreService.deleteGenreByName(name);
    }

    //Books
    @ShellMethod(key = {"books-count", "bc"}, value = "Show books count")
    public long getBooksCount() {
        return bookService.showBooksCount();
    }

    @ShellMethod(key = {"books-get", "bg"}, value = "Show all books")
    public String getAllBooks() {
        return bookService.showAllBooks();
    }

    @ShellMethod(key = {"book-get-id", "bgi"}, value = "Show book by id")
    public String getBookById(long id) {
        return bookService.showBookById(id);
    }

    @ShellMethod(key = {"book-get-name", "bgn"}, value = "Show book by name")
    public String getBookByName(String name) {
        return bookService.showBookByName(name);
    }

    @ShellMethod(key = {"book-get-genre-id", "bggi"}, value = "Show books by genre id")
    public String getBooksByGenreId(Long genreId) {
        return bookService.showBooksByGenreId(genreId);
    }

    @ShellMethod(key = {"book-get-genre-name", "bggn"}, value = "Show books by genre name")
    public String getBooksByGenreName(String genreName) {
        return bookService.showBooksByGenreName(genreName);
    }

    @ShellMethod(key = {"book-add", "ba"}, value = "Add new book")
    public String addBook(String bookName, String authorName, String... genreNames) {
        return bookService.addBook(bookName, authorName, genreNames);
    }

    @ShellMethod(key = {"book-del-name", "bdn"}, value = "Remove book by name")
    public String delBookByName(String name) {
        return bookService.deleteBookByName(name);
    }

    //Comments
    @ShellMethod(key = {"comment-count-book-id", "ccbi"}, value = "Show comments count by book id")
    public String getCommentsCountByBookId(long bookId) {
        return commentService.showCountByBookId(bookId);
    }

    @ShellMethod(key = {"comment-count-book-name", "ccbn"}, value = "Show comments count by book name")
    public String getCommentsCountByBookName(String bookName) {
        return commentService.showCountByBookName(bookName);
    }

    @ShellMethod(key = {"comment-get-id", "cgi"}, value = "Show comment by id")
    public String getCommentById(long id) {
        return commentService.showById(id);
    }

    @ShellMethod(key = {"comment-get-book-id", "cgbi"}, value = "Show comments by book id")
    public String getCommentsByBookId(long bookId) {
        return commentService.showByBookId(bookId);
    }

    @ShellMethod(key = {"comment-get-book-name", "cgbn"}, value = "Show comments by book name")
    public String getCommentsByBookName(String bookName) {
        return commentService.showByBookName(bookName);
    }

    @ShellMethod(key = {"comment-add-book-name", "cabn"}, value = "Add comment by book name")
    public String addCommentByBookName(String bookName, String text) {
        return commentService.addByBookName(bookName, text);
    }
    @ShellMethod(key = {"comment-upd-id", "cui"}, value = "Update comment by id")
    public String updCommentById(long id, String text) {
        return commentService.updateById(id, text);
    }

    @ShellMethod(key = {"comment-del-id", "cdi"}, value = "Delete comment by id")
    public String delCommentById(long id) {
        return commentService.deleteById(id);
    }
}
