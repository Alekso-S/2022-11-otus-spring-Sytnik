package ru.otus.spring.mongock.changelog.util;

import ru.otus.spring.model.mongo.MongoAuthor;
import ru.otus.spring.model.mongo.MongoBook;
import ru.otus.spring.model.mongo.MongoComment;
import ru.otus.spring.model.mongo.MongoGenre;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("SpellCheckingInspection")
public class DataProducer {

    private final static List<MongoAuthor> authors = Stream.of(
                    "Stephen King",
                    "Viktor Pelevin",
                    "Strugatsky Brothers"
            )
            .map(MongoAuthor::new)
            .collect(Collectors.toList());

    private final static List<MongoGenre> genres = Stream.of(
                    "Horror",
                    "Thriller",
                    "Dark fantasy",
                    "Coming-of-age story",
                    "Science fiction",
                    "Western",
                    "Absurdist fiction",
                    "Satire",
                    "Novel"
            )
            .map(MongoGenre::new)
            .collect(Collectors.toList());

    private final static List<MongoComment> comments = Stream.of(
                    "All or nothing",
                    "A little bird told me…",
                    "Don’t judge a book by it’s cover",
                    "To live a cat and dog life",
                    "Kill two birds with one stone",
                    "Your roof is running away from you",
                    "You rock!",
                    "Nobody’s perfect, but me",
                    "Things don’t always work out the first time",
                    "Being between hammer and anvil",
                    "After a storm comes a calm",
                    "The sun will shine on our side of the fence",
                    "The right man in the right place",
                    "Better late than never",
                    "If there is a will, there is a way",
                    "A good heart’s worth gold",
                    "Happiness is not a destination. It is a method of life",
                    "An hour in the morning is worth two in the evening",
                    "It takes two to tango",
                    "It takes two to lie. One to lie and one to listen",
                    "Success does not consist in never making mistakes but in never making the same one a second time",
                    "Simplicity is the ultimate sophistication",
                    "Always forgive your enemies; nothing annoys them so much",
                    "Your time is limited, so don’t waste it living someone else’s life",
                    "No two persons ever read the same book",
                    "While we are postponing, life speeds by",
                    "Build your own dreams, or someone else will hire you to build theirs"
            )
            .map(MongoComment::new)
            .collect(Collectors.toList());

    private final static List<MongoBook> books = List.of(
            new MongoBook("'Salem's Lot", authors.get(0), List.of(
                    getGenreByName("Horror")
            )),
            new MongoBook("It", authors.get(0), List.of(
                    getGenreByName("Horror"),
                    getGenreByName("Thriller"),
                    getGenreByName("Dark fantasy"),
                    getGenreByName("Coming-of-age story")
            )),
            new MongoBook("The Dark Tower", authors.get(0), List.of(
                    getGenreByName("Dark fantasy"),
                    getGenreByName("Science fiction"),
                    getGenreByName("Horror"),
                    getGenreByName("Western")
            )),
            new MongoBook("Omon Ra", authors.get(1), List.of(
                    getGenreByName("Science fiction"),
                    getGenreByName("Absurdist fiction"),
                    getGenreByName("Satire")
            )),
            new MongoBook("Chapayev and Void", authors.get(1), List.of(
                    getGenreByName("Novel")
            )),
            new MongoBook("Generation \"P\"", authors.get(1), List.of(
                    getGenreByName("Novel")
            )),
            new MongoBook("Hard to Be a God", authors.get(2), List.of(
                    getGenreByName("Science fiction")
            )),
            new MongoBook("Monday Begins on Saturday", authors.get(2), List.of(
                    getGenreByName("Science fiction")
            )),
            new MongoBook("Roadside Picnic", authors.get(2), List.of(
                    getGenreByName("Science fiction")
            ))
    );

    public static List<MongoBook> getAllBooks() {
        return books;
    }

    public static Map<String, List<MongoComment>> getBookCommentsMap() {
        return Map.of(
                books.get(0).getName(), List.of(comments.get(0), comments.get(9), comments.get(18)),
                books.get(1).getName(), List.of(comments.get(1), comments.get(10), comments.get(19)),
                books.get(2).getName(), List.of(comments.get(2), comments.get(11), comments.get(20)),
                books.get(3).getName(), List.of(comments.get(3), comments.get(12), comments.get(21)),
                books.get(4).getName(), List.of(comments.get(4), comments.get(13), comments.get(22)),
                books.get(5).getName(), List.of(comments.get(5), comments.get(14), comments.get(23)),
                books.get(6).getName(), List.of(comments.get(6), comments.get(15), comments.get(24)),
                books.get(7).getName(), List.of(comments.get(7), comments.get(16), comments.get(25)),
                books.get(8).getName(), List.of(comments.get(8), comments.get(17), comments.get(26))
        );
    }

    private static MongoGenre getGenreByName(String genreName) {
        return genres.stream()
                .filter(genre -> genre.getName().equals(genreName))
                .findFirst().orElseThrow();
    }
}
