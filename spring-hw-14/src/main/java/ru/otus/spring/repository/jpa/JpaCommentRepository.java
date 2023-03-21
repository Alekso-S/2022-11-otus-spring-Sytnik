package ru.otus.spring.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.jpa.JpaComment;

public interface JpaCommentRepository extends JpaRepository<JpaComment, Long> {
}
