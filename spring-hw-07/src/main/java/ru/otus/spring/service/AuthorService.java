package ru.otus.spring.service;

public interface AuthorService {
    long showCount();

    String showAll();

    String showById(long id);

    String showByName(String name);

    String add(String name);

    String deleteByName(String name);
}
