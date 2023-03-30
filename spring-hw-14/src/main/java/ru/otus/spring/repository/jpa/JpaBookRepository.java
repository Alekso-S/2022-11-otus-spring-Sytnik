package ru.otus.spring.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.jpa.JpaBook;

import java.util.Optional;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {
    Optional<JpaBook> findByName(String name);
}
