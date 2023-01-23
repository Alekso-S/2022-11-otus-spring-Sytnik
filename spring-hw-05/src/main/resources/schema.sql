DROP TABLE IF EXISTS authors;
CREATE TABLE authors (
        id          BIGINT      AUTO_INCREMENT  PRIMARY KEY
     ,  name        VARCHAR(255)                UNIQUE                  NOT NULL
);

DROP TABLE IF EXISTS genres;
CREATE TABLE genres (
        id          BIGINT      AUTO_INCREMENT  PRIMARY KEY
    ,   name        VARCHAR(255)                UNIQUE                  NOT NULL
);

DROP TABLE IF EXISTS books;
CREATE TABLE books (
        id          BIGINT      AUTO_INCREMENT  PRIMARY KEY
    ,   author_id   BIGINT                      REFERENCES authors(id)  NOT NULL
    ,   name        VARCHAR(255)                UNIQUE                  NOT NULL
);

DROP TABLE IF EXISTS books_genres;
CREATE TABLE books_genres (
        book_id     BIGINT                      REFERENCES books(id)    NOT NULL
    ,   genre_id    BIGINT                      REFERENCES genres(id)   NOT NULL
);