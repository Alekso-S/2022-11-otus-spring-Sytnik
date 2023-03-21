package ru.otus.spring.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.jpa.JpaGenre;

import java.util.Optional;

public interface JpaGenreRepository extends JpaRepository<JpaGenre, Long> {
    Optional<JpaGenre> findByName(String name);
}
