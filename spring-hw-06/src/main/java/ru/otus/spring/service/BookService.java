package ru.otus.spring.service;

public interface BookService {
    long showBooksCount();

    String showAllBooks();

    String showBookById(long id);

    String showBookByName(String name);

    String showBooksByGenreId(long genreId);

    String showBooksByGenreName(String genreName);

    String addBook(String name, String authorName, String[] genreNames);

    String delBookByName(String name);
}
