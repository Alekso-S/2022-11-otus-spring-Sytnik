package ru.otus.spring.repository;

import ru.otus.spring.domain.Book;

public interface BookRepositoryCustom {
    void deleteWithComments(Book book);
}
