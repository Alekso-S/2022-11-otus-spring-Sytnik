INSERT INTO authors (`name`)
VALUES      ('Author 1')
        ,   ('Author 2');

INSERT INTO genres (`name`)
VALUES      ('Genre 1')
        ,   ('Genre 2')
        ,   ('Genre 3')
        ,   ('Genre 4');

INSERT INTO books (`author_id`,`name`)
VALUES      (1, 'Book 1')
        ,   (1, 'Book 2')
        ,   (2, 'Book 3')
        ,   (2, 'Book 4');

INSERT INTO books_genres (`book_id`,`genre_id`)
VALUES      ((SELECT id FROM books WHERE name = 'Book 1'), (SELECT id FROM genres WHERE name='Genre 1'))
        ,   ((SELECT id FROM books WHERE name = 'Book 1'), (SELECT id FROM genres WHERE name='Genre 2'))
        ,   ((SELECT id FROM books WHERE name = 'Book 2'), (SELECT id FROM genres WHERE name='Genre 2'))
        ,   ((SELECT id FROM books WHERE name = 'Book 2'), (SELECT id FROM genres WHERE name='Genre 3'))
        ,   ((SELECT id FROM books WHERE name = 'Book 3'), (SELECT id FROM genres WHERE name='Genre 3'))
        ,   ((SELECT id FROM books WHERE name = 'Book 3'), (SELECT id FROM genres WHERE name='Genre 4'))
        ,   ((SELECT id FROM books WHERE name = 'Book 4'), (SELECT id FROM genres WHERE name='Genre 1'))
        ,   ((SELECT id FROM books WHERE name = 'Book 4'), (SELECT id FROM genres WHERE name='Genre 4'));

INSERT INTO comments (`book_id`,`text`)
VALUES      (1, 'Comment 1 text')
        ,   (1, 'Comment 2 text')
        ,   (2, 'Comment 3 text')
        ,   (2, 'Comment 4 text')
        ,   (3, 'Comment 5 text')
        ,   (3, 'Comment 6 text')
        ,   (4, 'Comment 7 text')
        ,   (4, 'Comment 8 text');