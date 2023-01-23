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
        ,   ((SELECT id FROM books WHERE name = 'Book 4'), (SELECT id FROM genres WHERE name='Genre 4'))
        ,   ((SELECT id FROM books WHERE name = 'Book 4'), (SELECT id FROM genres WHERE name='Genre 1'));

