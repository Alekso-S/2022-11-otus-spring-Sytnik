package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

@RequiredArgsConstructor
@ShellComponent
public class Commands {

    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookService bookService;

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
        return authorService.delAuthorByName(name);
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
        return genreService.delGenreByName(name);
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
        return bookService.delBookByName(name);
    }
}
