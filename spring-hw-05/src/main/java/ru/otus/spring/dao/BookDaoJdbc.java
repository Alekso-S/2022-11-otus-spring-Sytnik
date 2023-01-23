package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.BookNotFoundEx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM books";
        return jdbcOperations.getJdbcOperations().queryForObject(query, Long.class);
    }

    @Override
    public List<Book> getAll() {
        String query = "SELECT id, author_id, name FROM books ORDER BY id";
        return jdbcOperations.query(query, new BookMapper());
    }

    @Override
    public Book getById(long id) throws BookNotFoundEx {
        String query = "SELECT id, author_id, name FROM books WHERE id=:id";
        try {
            return jdbcOperations.queryForObject(query, Map.of("id", id), new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new BookNotFoundEx(e.getMessage());
        }
    }

    @Override
    public Book getByName(String name) throws BookNotFoundEx {
        String query = "SELECT id, author_id, name FROM books WHERE name=:name";
        try {
            return jdbcOperations.queryForObject(query, Map.of("name", name), new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new BookNotFoundEx(e.getMessage());
        }
    }

    @Override
    public List<Book> getByGenreId(long id) {
        String query = "SELECT B.id, B.author_id, B.name FROM books B JOIN books_genres BG ON BG.book_id=B.id AND BG.genre_id=:id";
        return jdbcOperations.query(query, Map.of("id", id), new BookMapper());
    }

    @Override
    public List<Book> getByAuthorId(long authorId) {
        String query = "SELECT id, author_id, name FROM books WHERE author_id=:author_id";
        return jdbcOperations.query(query, Map.of("author_id", authorId), new BookMapper());
    }

    @Override
    public Book add(Book book) {
        String query = "INSERT INTO books (name,author_id) VALUES (:name,:author_id)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", book.getName())
                .addValue("author_id", book.getAuthorId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(query, params, keyHolder);
        return new Book(Objects.requireNonNull(keyHolder.getKey()).longValue(), book.getAuthorId(), book.getName());
    }

    @Override
    public void delByName(String name) {
        String query = "DELETE FROM books WHERE name=:name";
        jdbcOperations.update(query, Map.of("name", name));
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            long authorId = resultSet.getLong("author_id");
            String name = resultSet.getString("name");
            return new Book(id, authorId, name);
        }
    }
}
