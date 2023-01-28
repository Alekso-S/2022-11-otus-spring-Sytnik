package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.AuthorHasRelationsEx;
import ru.otus.spring.exception.AuthorNotFoundEx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM authors";
        return jdbcOperations.getJdbcOperations().queryForObject(query, Long.class);
    }

    @Override
    public List<Author> getAll() {
        String query = "SELECT id, name FROM authors ORDER BY id";
        return jdbcOperations.query(query, new AuthorMapper());
    }

    @Override
    public Author getById(long id) throws AuthorNotFoundEx {
        String query = "SELECT id, name FROM authors WHERE id=:id";
        try {
            return jdbcOperations.queryForObject(query, Map.of("id", id), new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorNotFoundEx(e.getMessage());
        }
    }

    @Override
    public Author getByName(String name) throws AuthorNotFoundEx {
        String query = "SELECT id, name FROM authors WHERE name=:name";
        try {
            return jdbcOperations.queryForObject(query, Map.of("name", name), new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorNotFoundEx(e.getMessage());
        }
    }

    @Override
    public Author add(Author author) {
        String query = "INSERT INTO authors (name) VALUES (:name)";
        MapSqlParameterSource params = new MapSqlParameterSource("name", author.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(query, params, keyHolder);
        return new Author(Objects.requireNonNull(keyHolder.getKey()).longValue(), author.getName());
    }

    @Override
    public void delByName(String name) throws AuthorHasRelationsEx, AuthorNotFoundEx {
        String query = "DELETE FROM authors WHERE name=(:name)";
        if (checkRelationsByName(name)) {
            throw new AuthorHasRelationsEx("");
        } else if (jdbcOperations.update(query, Map.of("name", name)) == 0) {
            throw new AuthorNotFoundEx("");
        }
    }

    private boolean checkRelationsByName(String name) {
        String query = "" +
                "SELECT     COUNT(*)                                AS cnt " +
                "FROM       books   B " +
                "JOIN       authors A   ON  A.id = B.author_id" +
                "                       AND A.name = :name ";
        Long cnt = jdbcOperations.queryForObject(query, Map.of("name", name), Long.class);
        return cnt > 0;
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            return new Author(rs.getLong("id"), rs.getString("name"));
        }
    }
}
