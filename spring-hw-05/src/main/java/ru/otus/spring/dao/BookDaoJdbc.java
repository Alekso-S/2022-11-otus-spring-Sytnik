package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.dao.model.BookGenreRelation;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.BookNotFoundEx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final GenreDao genreDao;

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM books";
        return jdbcOperations.getJdbcOperations().queryForObject(query, Long.class);
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = getAllBooks();
        List<Genre> genres = genreDao.getAllUsed();
        List<BookGenreRelation> relations = getAllRelations();
        addGenresForBooks(books, genres, relations);
        return books;
    }

    @Override
    public Book getById(long id) throws BookNotFoundEx {
        String query = "" +
                "SELECT     B.id                                    AS book_id " +
                "       ,   B.name                                  AS book_name " +
                "       ,   A.id                                    AS author_id " +
                "       ,   A.name                                  AS author_name " +
                "       ,   G.id                                    AS genre_id " +
                "       ,   G.name                                  AS genre_name " +
                "FROM       books           B " +
                "JOIN       authors         A   ON A.id = B.author_id " +
                "LEFT JOIN  books_genres    BG  ON BG.book_id = B.id " +
                "LEFT JOIN  genres          G   ON G.id = BG.genre_id " +
                "WHERE      B.id = :id";
        try {
            return jdbcOperations.query(query, Map.of("id", id), new BookExtractor()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new BookNotFoundEx(e.getMessage());
        }
    }

    @Override
    public Book getByName(String name) throws BookNotFoundEx {
        String query = "" +
                "SELECT     B.id                                    AS book_id " +
                "       ,   B.name                                  AS book_name " +
                "       ,   A.id                                    AS author_id " +
                "       ,   A.name                                  AS author_name " +
                "       ,   G.id                                    AS genre_id " +
                "       ,   G.name                                  AS genre_name " +
                "FROM       books           B " +
                "JOIN       authors         A   ON A.id = B.author_id " +
                "LEFT JOIN  books_genres    BG  ON BG.book_id = B.id " +
                "LEFT JOIN  genres          G   ON G.id = BG.genre_id " +
                "WHERE      B.name = :name";
        try {
            return jdbcOperations.query(query, Map.of("name", name), new BookExtractor()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new BookNotFoundEx(e.getMessage());
        }
    }

    @Override
    public List<Book> getByGenreId(long genreId) {
        String query = "" +
                "SELECT     B.id                                    AS book_id " +
                "       ,   B.name                                  AS book_name " +
                "       ,   A.id                                    AS author_id " +
                "       ,   A.name                                  AS author_name " +
                "       ,   G.id                                    AS genre_id " +
                "       ,   G.name                                  AS genre_name " +
                "FROM       books           B " +
                "JOIN       authors         A   ON  A.id = B.author_id " +
                "LEFT JOIN  books_genres    BG  ON  BG.book_id = B.id " +
                "LEFT JOIN  genres          G   ON  G.id = BG.genre_id " +
                "JOIN       books_genres    BG2 ON  BG2.book_id = B.id " +
                "                               AND BG2.genre_id = :genre_id " +
                "ORDER BY   B.id, G.id";
        return jdbcOperations.query(query, Map.of("genre_id", genreId), new BookExtractor());
    }

    @Override
    public List<Book> getByAuthorId(long authorId) {
        String query = "" +
                "SELECT     B.id                                    AS book_id " +
                "       ,   B.name                                  AS book_name " +
                "       ,   A.id                                    AS author_id " +
                "       ,   A.name                                  AS author_name " +
                "       ,   G.id                                    AS genre_id " +
                "       ,   G.name                                  AS genre_name " +
                "FROM       books           B " +
                "JOIN       authors         A   ON A.id = B.author_id " +
                "LEFT JOIN  books_genres    BG  ON BG.book_id = B.id " +
                "LEFT JOIN  genres          G   ON G.id = BG.genre_id " +
                "WHERE      A.id = :author_id " +
                "ORDER BY   B.id, G.id";
        return jdbcOperations.query(query, Map.of("author_id", authorId), new BookExtractor());
    }

    @Override
    public Book add(Book book) {
        String bookQuery = "INSERT INTO books (name,author_id) VALUES (:name,:author_id)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", book.getName())
                .addValue("author_id", book.getAuthor().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(bookQuery, params, keyHolder);
        return new Book(Objects.requireNonNull(keyHolder.getKey()).longValue(),
                book.getName(), book.getAuthor(), book.getGenres());
    }

    @Override
    public void delByName(String name) throws BookNotFoundEx {
        genreDao.delGenresByBookName(name);
        String query = "DELETE FROM books WHERE name=:name";
        if (jdbcOperations.update(query, Map.of("name", name)) == 0) {
            throw new BookNotFoundEx("");
        }
    }

    private List<BookGenreRelation> getAllRelations() {
        String query = "SELECT book_id, genre_id FROM books_genres";
        return jdbcOperations.query(query, (rs, rowNum) ->
                new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id")));
    }

    private List<Book> getAllBooks() {
        String query = "" +
                "SELECT     B.id                                    AS book_id " +
                "       ,   B.name                                  AS book_name " +
                "       ,   A.id                                    AS author_id " +
                "       ,   A.name                                  AS author_name " +
                "FROM       books           B " +
                "JOIN       authors         A   ON A.id = B.author_id " +
                "ORDER BY   B.id";
        return jdbcOperations.query(query, new BookMapper());
    }

    private void addGenresForBooks(List<Book> books, List<Genre> genres, List<BookGenreRelation> relations) {
        books.forEach(b -> genres.stream()
                .filter(g -> relations.stream()
                        .filter(r -> r.getBookId() == b.getId())
                        .anyMatch(r -> r.getGenreId() == g.getId()))
                .forEach(g -> b.getGenres().add(g)));
    }

    private static class BookExtractor implements ResultSetExtractor<List<Book>> {
        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> books = new HashMap<>();
            while (rs.next()) {
                long id = rs.getLong("book_id");
                Book book = books.get(id);
                if (book == null) {
                    book = new Book(id,
                            rs.getString("book_name"),
                            new Author(rs.getLong("author_id"), rs.getString("author_name")),
                            new ArrayList<>());
                    books.put(id, book);
                }
                book.getGenres().add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }
            return new ArrayList<>(books.values());
        }
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(rs.getLong("book_id"),
                    rs.getString("book_name"),
                    new Author(rs.getLong("author_id"), rs.getString("author_name")),
                    new ArrayList<>());
        }
    }
}
