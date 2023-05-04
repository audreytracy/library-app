DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS book_inventory CASCADE;
DROP TABLE IF EXISTS holds CASCADE;
DROP TABLE IF EXISTS borrowing_history CASCADE;
DROP TABLE IF EXISTS book CASCADE;
DROP TABLE IF EXISTS author CASCADE;
DROP TABLE IF EXISTS book_list_data CASCADE;
DROP TABLE IF EXISTS avail_copies_data CASCADE;

CREATE TABLE account( account_id SERIAL PRIMARY KEY, fname VARCHAR(50), lname VARCHAR(50), phone BIGINT, pin INTEGER CHECK (pin >= 1000 AND pin <= 9999) NOT NULL, username VARCHAR(50) NOT NULL);

CREATE TABLE author( author_id SERIAL PRIMARY KEY, fname VARCHAR(50) NOT NULL, lname VARCHAR(50));

CREATE TABLE book( book_id SERIAL PRIMARY KEY, author_id INT REFERENCES author(author_id) NOT NULL, title VARCHAR(50) NOT NULL, genre VARCHAR(30), summary VARCHAR(200), pages INT,  publication_date DATE);

CREATE TABLE book_inventory( book_id INT PRIMARY KEY, num_copies INT, FOREIGN KEY (book_id) REFERENCES book(book_id));

CREATE TABLE holds( book_id INT, account_id INT, time_placed TIMESTAMP DEFAULT CURRENT_TIMESTAMP, time_first_on_list TIMESTAMP, hold_expire TIMESTAMP GENERATED ALWAYS AS (time_first_on_list + INTERVAL '10 days') STORED, PRIMARY KEY (book_id, account_id), FOREIGN KEY (book_id) REFERENCES book(book_id), FOREIGN KEY (account_id) REFERENCES account(account_id));


CREATE OR REPLACE FUNCTION log_hold_time() RETURNS TRIGGER LANGUAGE PLPGSQL AS $$ BEGIN IF NEW.time_placed = (SELECT MIN(time_placed) FROM holds WHERE book_id = NEW.book_id) THEN UPDATE holds SET time_first_on_list = CURRENT_TIMESTAMP WHERE (account_id = NEW.account_id AND book_id = NEW.book_id); ELSEIF OLD.time_placed = (SELECT MIN(time_placed) FROM holds WHERE book_id = OLD.book_id) THEN UPDATE holds SET time_first_on_list = CURRENT_TIMESTAMP WHERE (book_id = OLD.book_id AND time_placed = (SELECT MIN(time_placed) FROM holds WHERE book_id = OLD.book_id and time_placed > OLD.time_placed)); END IF; RETURN OLD; END; $$;

CREATE TRIGGER log_hold AFTER INSERT ON holds FOR EACH ROW EXECUTE PROCEDURE log_hold_time();

CREATE TRIGGER change_hold BEFORE DELETE ON holds FOR EACH ROW EXECUTE PROCEDURE log_hold_time();

CREATE TABLE borrowing_history(checkout_id SERIAL PRIMARY KEY, book_id INT NOT NULL, account_id INT NOT NULL, date_checked_out DATE DEFAULT CURRENT_DATE, due_date DATE GENERATED ALWAYS AS (date_checked_out + INTERVAL '30 days') STORED, date_returned DATE, FOREIGN KEY (book_id) REFERENCES book(book_id), FOREIGN KEY (account_id) REFERENCES account(account_id));

CREATE VIEW book_list_data AS SELECT book.*, fname, lname FROM book JOIN author ON book.author_id = author.author_id GROUP BY title, fname, lname, book.book_id, summary, pages, genre, book.author_id; 

CREATE VIEW avail_copies_data AS SELECT book_inventory.book_id, num_copies as total_copies, sum(CASE WHEN (borrowing_history.date_returned IS NULL AND borrowing_history.date_checked_out IS NOT NULL) THEN 1 ELSE 0 END) as in_use_copies FROM book_inventory LEFT JOIN borrowing_history ON book_inventory.book_id = borrowing_history.book_id GROUP BY book_inventory.book_id; 

INSERT INTO author(fname, lname) VALUES ('J.K.', 'Rowling'), ('George', 'Orwell'), ('Harper', 'Lee'), ('Margaret', 'Atwood'), ('Mark', 'Twain'), ('F. Scott', 'Fitzgerald'), ('Leo', 'Tolstoy'), ('Ernest', 'Hemingway'), ('Gabriel', 'García Márquez'), ('Toni', 'Morrison'), ('Kurt', 'Vonnegut'), ('Ray', 'Bradbury'), ('J.D.', 'Salinger'), ('Chinua', 'Achebe'), ('Stephen', 'King'), ('Donna', 'Tartt'), ('Angie', 'Thomas'), ('Ernest', 'Cline'), ('Sharon', 'Moalem');

INSERT INTO book(author_id, title, genre, summary, pages, publication_date) VALUES (1, 'Harry Potter and the Sorcerer''s Stone', 'Fantasy', 'The story of a young wizard who discovers his magical heritage and battles against the dark lord Voldemort.', 223, '1997-06-26'), (2, '1984', 'Dystopian Fiction', 'The story takes place in a totalitarian society where the government controls every aspect of people''s lives.', 328, '1949-06-08'), (3, 'To Kill a Mockingbird', 'Fiction', 'The novel is set in the 1930s and tells the story of a young girl growing up in a racially divided community in Alabama.', 281, '1960-07-11'), (4, 'The Handmaid''s Tale', 'Dystopian Fiction', 'The novel is set in a future where women are treated as property and used only for procreation.', 311, '1985-06-14'), (5, 'The Adventures of Huckleberry Finn', 'Adventure Fiction', 'The novel is set in the southern United States during the 1840s and follows the adventures of a young boy and his friend, a runaway slave.', 366, '1884-12-10'), (6, 'The Great Gatsby', 'Fiction', 'The novel is set in the Roaring Twenties and tells the story of a mysterious millionaire, Jay Gatsby, and his obsession with a married woman, Daisy Buchanan.', 180, '1925-04-10'), (7, 'War and Peace', 'Historical Fiction', 'The novel is set during the Napoleonic Wars and follows several aristocratic families as they deal with love, loss, and war.', 1225, '1869-01-01'), (8, 'The Old Man and the Sea', 'Novella', 'The story is about an aging fisherman who catches a giant marlin and struggles to bring it back to shore.', 127, '1952-09-01'), (9, 'One Hundred Years of Solitude', 'Magical Realism', 'The novel follows the Buendía family through seven generations in the fictional town of Macondo.', 417, '1967-05-30'), (10, 'Beloved', 'Historical Fiction', 'The novel is set after the American Civil War and follows the story of a former slave, Sethe, and the haunting of her home by the ghost of her baby.', 275, '1987-09-02'), (11, 'Slaughterhouse-Five', 'Science Fiction', 'The novel is based on Vonnegut''s own experiences as a prisoner of war during the firebombing of Dresden in World War II.', 215, '1969-03-31'), (12, 'Fahrenheit 451', 'Dystopian Fiction', 'The novel is set in a future where books are banned and "firemen" burn any that are found.', 158, '1953-10-19'), (13, 'The Catcher in the Rye', 'Coming-of-age Story', 'The novel is about a teenage boy named Holden Caulfield who is struggling with the transition from adolescence to adulthood.', 234, '1951-07-16'), (14, 'Things Fall Apart', 'Historical Fiction', 'The novel is set in pre-colonial Nigeria and follows the story of Okonkwo, a proud and respected leader in his village.', 209, '1958-06-01'), (15, 'The Shining', 'Horror', 'The novel is about a family that becomes trapped in a remote hotel during the winter and is haunted by supernatural forces.', 447, '1977-01-28'), (16, 'The Goldfinch', 'Mystery', 'The novel follows the life of a young boy named Theo Decker after he survives a terrorist attack at an art museum.', 771, '2013-10-22'), (1, 'Harry Potter and the Chamber of Secrets', 'Fantasy', 'The novel is the second in the Harry Potter series and follows the story of Harry Potter and his friends as they try to uncover the mystery of the Chamber of Secrets.', 251, '1998-07-02'), (17, 'The Hate U Give', 'Young Adult Fiction', 'The novel tells the story of Starr Carter, a 16-year-old black girl who witnesses the fatal shooting of her unarmed best friend by a white police officer.', 464, '2017-02-28'), (18, 'Ready Player One', 'Science Fiction', 'In 2045, the creator of a virtual reality world called the OASIS dies, leaving behind a series of puzzles that lead to his fortune.', 386, '2011-08-16'), (19, 'Survival of the Sickest', 'Nonfiction', 'A book about the surprising ways in which diseases have helped humans survive and evolve', 320, '2020-06-01');

INSERT INTO book_inventory(book_id, num_copies) VALUES (1, 3), (2, 4), (3, 1), (4, 4), (5, 5), (6, 7), (7, 2), (8, 1), (9, 1), (10, 1), (11, 1), (12, 3), (13, 2), (14, 2), (15, 2), (16, 2), (17, 3), (18, 4), (19, 1), (20, 2); 

INSERT INTO account (fname, lname, phone, pin, username) VALUES ('John', 'Doe', 1234567890, 1234, 'johndoe'), ('Jane', 'Doe', 9876543210, 5678, 'janedoe'), ('Bob', 'Smith', 5555555555, 2468, 'bobsmith');

INSERT INTO holds (book_id, account_id, time_placed) VALUES (1, 1, '2023-04-24 15:30:45'), (1, 2, '2023-04-14 15:30:45'), (1, 3, '2023-04-26 15:30:45'), (3, 1, '2023-04-12 11:20:10'), (3, 3, '2023-04-07 19:45:30'), (14, 2, '2023-04-07 19:45:30'), (15, 2, '2022-12-31 23:59:59'), (16, 2, '2023-01-01 12:00:00'), (19, 2, '2022-11-22 06:30:15'), (20, 3, '2023-07-15 14:10:25'), (9, 3, '2022-08-04 09:45:30'), (8, 3, '2023-02-14 18:20:40');

INSERT INTO borrowing_history(book_id, account_id, date_checked_out, date_returned) VALUES (1,1,'2014-08-01', '2014-09-21'), (1,1,'2023-03-08', NULL), (1, 3, '2023-04-01', NULL), (1,2,'2023-04-11', NULL), (5, 1, '2022-04-20', '2022-04-30'), (14, 2, '2021-07-12', '2021-07-19'), (14, 2, '2023-04-12', NULL), (14, 2, '2023-05-02', NULL), (20, 3, '2019-11-05', '2019-11-11'), (20, 2, '2022-11-05', '2019-11-11'), (20, 2, '2023-01-05', '2019-11-11'), (10, 1, '2020-08-22', '2020-08-30'), (8, 2, '2018-05-15', '2018-05-21'), (11, 1, '2017-02-14', '2017-02-21'), (3, 2, '2018-11-10', '2018-11-18'), (3, 1, '2023-03-21', NULL), (19, 3, '2022-09-08', NULL), (11, 1, '2016-12-01', '2017-01-27'), (17, 2, '2020-03-20', '2020-03-28'), (9, 2, '2020-03-20', NULL), (19, 1, '2017-06-10', '2017-06-16'), (8, 1, '2023-05-02', NULL), (15, 1, '2023-04-30', NULL), (15, 2, '2023-05-01', NULL), (16, 2, '2019-02-14', '2019-02-22'), (16, 1, '2023-02-14', NULL), (16, 3, '2023-03-14', NULL), (7, 3, '2021-10-12', NULL);


CREATE OR REPLACE FUNCTION get_hold_pos(a_id IN INTEGER, b_id IN INTEGER) RETURNS INTEGER LANGUAGE PLPGSQL AS $$ BEGIN RETURN (SELECT  SUM(CASE WHEN time_placed < (SELECT time_placed FROM holds WHERE book_id = b_id AND account_id = a_id) THEN 1 ELSE 0 END) FROM book JOIN holds ON book.book_id = holds.book_id WHERE book.book_id = b_id); END; $$;

CREATE VIEW holds_data AS (SELECT holds.book_id, holds.account_id, holds.time_placed, num_copies, get_hold_pos(holds.account_id, holds.book_id) as hold_pos FROM holds JOIN book_inventory ON book_inventory.book_id = holds.book_id GROUP BY holds.book_id, holds.account_id, holds.time_placed, book_inventory.num_copies);


SELECT * FROM holds_data WHERE account_id = 2;
SELECT count(*) FROM holds WHERE account_id = 2;
SELECT * FROM holds_data LEFT JOIN book ON holds_data.book_id = book.book_id LEFT JOIN author ON author.author_id = book.author_id WHERE account_id = 2;