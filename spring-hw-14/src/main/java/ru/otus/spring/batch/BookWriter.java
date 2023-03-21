package ru.otus.spring.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.model.jpa.JpaAuthor;
import ru.otus.spring.model.jpa.JpaBook;
import ru.otus.spring.model.jpa.JpaGenre;
import ru.otus.spring.repository.jpa.JpaAuthorRepository;
import ru.otus.spring.repository.jpa.JpaBookRepository;
import ru.otus.spring.repository.jpa.JpaGenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookWriter implements ItemWriter<BookDto> {

    private final JpaAuthorRepository authorRepository;
    private final JpaGenreRepository genreRepository;
    private final JpaBookRepository bookRepository;

    @Override
    public void write(List<? extends BookDto> booksDtos) {

        List<JpaAuthor> authors = booksDtos.stream()
                .map(BookDto::getAuthorName)
                .distinct()
                .map(JpaAuthor::new)
                .collect(Collectors.toList());
        authorRepository.saveAll(authors);

        List<JpaGenre> genres = booksDtos.stream()
                .flatMap(bookDto2 -> bookDto2.getGenreNames().stream())
                .distinct()
                .map(JpaGenre::new)
                .collect(Collectors.toList());
        genreRepository.saveAll(genres);

        List<JpaBook> books = booksDtos.stream()
                .map(bookDto2 -> new JpaBook(
                        bookDto2.getName(),
                        authors.stream()
                                .filter(jpaAuthor -> jpaAuthor.getName().equals(bookDto2.getAuthorName()))
                                .findAny().orElseThrow(),
                        genres.stream()
                                .filter(jpaGenre -> bookDto2.getGenreNames().contains(jpaGenre.getName()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        bookRepository.saveAll(books);
    }
}
