package ru.otus.spring.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.jpa.JpaAuthor;

import java.util.Optional;

public interface JpaAuthorRepository extends JpaRepository<JpaAuthor, Long> {
    Optional<JpaAuthor> findByName(String name);
}
