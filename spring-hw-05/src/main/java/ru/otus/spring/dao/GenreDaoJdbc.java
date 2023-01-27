package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.GenreNotFoundEx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM genres";
        return jdbcOperations.getJdbcOperations().queryForObject(query, Long.class);
    }

    @Override
    public List<Genre> getAll() {
        String query = "SELECT id, name FROM genres ORDER BY id";
        return jdbcOperations.query(query, new GenreDaoJdbc.GenreMapper());
    }

    @Override
    public List<Genre> getAllUsed() {
        String query = "" +
                "SELECT     id " +
                "       ,   name " +
                "FROM       genres " +
                "WHERE      id IN (SELECT genre_id FROM books_genres)";
        return jdbcOperations.query(query, new GenreDaoJdbc.GenreMapper());
    }

    @Override
    public Genre getById(long id) throws GenreNotFoundEx {
        String query = "SELECT id, name FROM genres WHERE id=:id";
        try {
            return jdbcOperations.queryForObject(query, Map.of("id", id), new GenreDaoJdbc.GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundEx(e.getMessage());
        }
    }

    @Override
    public Genre getByName(String name) throws GenreNotFoundEx {
        String query = "SELECT id, name FROM genres WHERE name=:name";
        try {
            return jdbcOperations.queryForObject(query, Map.of("name", name), new GenreDaoJdbc.GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundEx(e.getMessage());
        }
    }

    @Override
    public List<Genre> getByBookId(long id) {
        String query = "SELECT G.id, G.name FROM genres G JOIN books_genres BG ON BG.genre_id=G.id AND BG.book_id=:id";
        return jdbcOperations.query(query, Map.of("id", id), new GenreDaoJdbc.GenreMapper());
    }

    @Override
    public Genre add(Genre genre) {
        String query = "INSERT INTO genres (name) VALUES (:name)";
        MapSqlParameterSource params = new MapSqlParameterSource("name", genre.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(query, params, keyHolder);
        return new Genre(Objects.requireNonNull(keyHolder.getKey()).longValue(), genre.getName());
    }

    @Override
    public void delByName(String name) {
        String query = "DELETE FROM genres WHERE name=(:name)";
        jdbcOperations.update(query, Map.of("name", name));
    }

    @Override
    public void addGenresForBook(long bookId, List<Genre> genres) {
        String query = "INSERT INTO books_genres (book_id,genre_id) VALUES (:book_id,:genre_id)";
        for (Genre genre : genres) {
            jdbcOperations.update(query, Map.of("book_id", bookId, "genre_id", genre.getId()));
        }
    }

    @Override
    public void delGenresForBook(long bookId) {
        String query = "DELETE FROM books_genres WHERE book_id=:book_id";
        jdbcOperations.update(query, Map.of("book_id", bookId));
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Genre(resultSet.getLong("id"),
                    resultSet.getString("name"));
        }
    }
}
