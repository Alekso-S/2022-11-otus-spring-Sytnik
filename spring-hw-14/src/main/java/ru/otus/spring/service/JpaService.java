package ru.otus.spring.service;

import ru.otus.spring.model.jpa.JpaBook;
import ru.otus.spring.model.jpa.JpaComment;
import ru.otus.spring.model.mongo.MongoBook;
import ru.otus.spring.model.mongo.MongoComment;

public interface JpaService {
    void erase();

    JpaBook prepareJpaBook(MongoBook book);

    JpaComment prepareJpaComment(MongoComment comment);
}
