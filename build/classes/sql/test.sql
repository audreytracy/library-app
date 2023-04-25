DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS book_inventory CASCADE;
DROP TABLE IF EXISTS holds CASCADE;
DROP TABLE IF EXISTS borrowing_history CASCADE;
DROP TABLE IF EXISTS book CASCADE;
DROP TABLE IF EXISTS author CASCADE;

CREATE TABLE account(
    account_id SERIAL PRIMARY KEY,
    fname VARCHAR(50),
    lname VARCHAR(50),
    phone BIGINT,
    pin INTEGER CHECK (pin >= 1000 AND pin <= 9999) NOT NULL, -- 4 digit pin
    username VARCHAR(50) NOT NULL
);

CREATE TABLE author(
    author_id SERIAL PRIMARY KEY,
    fname VARCHAR(50) NOT NULL,
    lname VARCHAR(50)
);

CREATE TABLE book(
    book_id SERIAL PRIMARY KEY, --used for cover photo too
    author_id INT REFERENCES author(author_id) NOT NULL, -- using REFERENCES bc don't need to care about delete operation since book records can't be deleted in my application
    title VARCHAR(50) NOT NULL,
    genre VARCHAR(30),
    summary VARCHAR(200),
    pages INT, 
    publication_date DATE
);

CREATE TABLE book_inventory(
    book_copy_id SERIAL PRIMARY KEY,
    book_id INT NOT NULL,
    date_added DATE, -- would probably ordinarily be DEFAULT CURRENT_DATE since its the date added to the library, but this system is assuming these books were added a while ago
    FOREIGN KEY (book_id) REFERENCES book (book_id)
);

CREATE TABLE holds(
    book_id INT,
    account_id INT, 
    time_placed TIMESTAMP DEFAULT CURRENT_DATE, -- date and time someone placed the hold
    -- if your timestamp is the oldest, you're first on the holds list.
    PRIMARY KEY (book_id, account_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);

CREATE TABLE borrowing_history(
    book_copy_id INT,
    account_id INT, 
    date_checked_out DATE DEFAULT CURRENT_DATE,
    due_date DATE GENERATED ALWAYS AS (date_checked_out + INTERVAL '30 days') STORED,
    date_returned DATE, -- optional, is null until user returns book
    PRIMARY KEY (book_copy_id, account_id)
);

-- INSERT DATA:

-- authors with books in library
INSERT INTO author(fname, lname) VALUES
('J.K.', 'Rowling'),
('George', 'Orwell'),
('Harper', 'Lee'),
('Margaret', 'Atwood'),
('Mark', 'Twain'),
('F. Scott', 'Fitzgerald'),
('Leo', 'Tolstoy'),
('Ernest', 'Hemingway'),
('Gabriel', 'García Márquez'),
('Toni', 'Morrison'),
('Kurt', 'Vonnegut'),
('Ray', 'Bradbury'),
('J.D.', 'Salinger'),
('Chinua', 'Achebe'),
('Stephen', 'King'),
('Donna', 'Tartt'),
('Angie', 'Thomas'),
('Ernest', 'Cline'),
('Sharon', 'Moalem');

-- books in library
INSERT INTO book(author_id, title, genre, summary, pages, publication_date) VALUES 
(1, 'Harry Potter and the Sorcerer''s Stone', 'Fantasy', 'The story of a young wizard who discovers his magical heritage and battles against the dark lord Voldemort.', 223, '1997-06-26'), 
(2, '1984', 'Dystopian Fiction', 'The story takes place in a totalitarian society where the government controls every aspect of people''s lives.', 328, '1949-06-08'),
(3, 'To Kill a Mockingbird', 'Fiction', 'The novel is set in the 1930s and tells the story of a young girl growing up in a racially divided community in Alabama.', 281, '1960-07-11'),
(4, 'The Handmaid''s Tale', 'Dystopian Fiction', 'The novel is set in a future where women are treated as property and used only for procreation.', 311, '1985-06-14'),
(5, 'The Adventures of Huckleberry Finn', 'Adventure Fiction', 'The novel is set in the southern United States during the 1840s and follows the adventures of a young boy and his friend, a runaway slave.', 366, '1884-12-10'),
(6, 'The Great Gatsby', 'Fiction', 'The novel is set in the Roaring Twenties and tells the story of a mysterious millionaire, Jay Gatsby, and his obsession with a married woman, Daisy Buchanan.', 180, '1925-04-10'),
(7, 'War and Peace', 'Historical Fiction', 'The novel is set during the Napoleonic Wars and follows several aristocratic families as they deal with love, loss, and war.', 1225, '1869-01-01'),
(8, 'The Old Man and the Sea', 'Novella', 'The story is about an aging fisherman who catches a giant marlin and struggles to bring it back to shore.', 127, '1952-09-01'),
(9, 'One Hundred Years of Solitude', 'Magical Realism', 'The novel follows the Buendía family through seven generations in the fictional town of Macondo.', 417, '1967-05-30'),
(10, 'Beloved', 'Historical Fiction', 'The novel is set after the American Civil War and follows the story of a former slave, Sethe, and the haunting of her home by the ghost of her baby.', 275, '1987-09-02'),
(11, 'Slaughterhouse-Five', 'Science Fiction', 'The novel is based on Vonnegut''s own experiences as a prisoner of war during the firebombing of Dresden in World War II.', 215, '1969-03-31'),
(12, 'Fahrenheit 451', 'Dystopian Fiction', 'The novel is set in a future where books are banned and "firemen" burn any that are found.', 158, '1953-10-19'),
(13, 'The Catcher in the Rye', 'Coming-of-age Story', 'The novel is about a teenage boy named Holden Caulfield who is struggling with the transition from adolescence to adulthood.', 234, '1951-07-16'), 
(14, 'Things Fall Apart', 'Historical Fiction', 'The novel is set in pre-colonial Nigeria and follows the story of Okonkwo, a proud and respected leader in his village.', 209, '1958-06-01'),
(15, 'The Shining', 'Horror', 'The novel is about a family that becomes trapped in a remote hotel during the winter and is haunted by supernatural forces.', 447, '1977-01-28'),
(16, 'The Goldfinch', 'Mystery', 'The novel follows the life of a young boy named Theo Decker after he survives a terrorist attack at an art museum.', 771, '2013-10-22'),
(1, 'Harry Potter and the Chamber of Secrets', 'Fantasy', 'The novel is the second in the Harry Potter series and follows the story of Harry Potter and his friends as they try to uncover the mystery of the Chamber of Secrets.', 251, '1998-07-02'),
(17, 'The Hate U Give', 'Young Adult Fiction', 'The novel tells the story of Starr Carter, a 16-year-old black girl who witnesses the fatal shooting of her unarmed best friend by a white police officer.', 464, '2017-02-28'),
(18, 'Ready Player One', 'Science Fiction', 'In 2045, the creator of a virtual reality world called the OASIS dies, leaving behind a series of puzzles that lead to his fortune.', 386, '2011-08-16'),
(19, 'Survival of the Sickest', 'Nonfiction', 'A book about the surprising ways in which diseases have helped humans survive and evolve', 320, '2020-06-01');
;

-- book inventory
INSERT INTO book_inventory(book_id, date_added) VALUES
(1, '2001-09-15'), -- 1
(1, '2001-09-14'), -- 2
(1, '2001-09-15'), -- 3
(2, '2010-05-08'), -- 4
(3, '1995-12-01'), -- 5
(4, '2003-10-11'), -- 6
(5, '1989-07-22'), -- 7
(6, '1997-03-20'), -- 8
(7, '2005-08-18'), -- 9
(8, '2011-11-13'), -- 10
(9, '1975-06-09'), -- 11
(10, '1999-02-01'), -- 12
(11, '2008-04-03'), -- 13
(12, '2015-09-30'), -- 14
(13, '2006-12-24'), -- 15
(14, '2014-03-15'), -- 16
(15, '1990-11-11'), -- 17
(16, '2002-06-07'), -- 18
(17, '2017-10-05'), -- 19
(18, '2019-06-18'), -- 20
(1, '2003-02-19'), -- 21
(2, '2015-09-12'), -- 22
(3, '1999-11-30'), -- 23
(4, '2012-03-01'), -- 24
(5, '2008-09-05'), -- 25
(6, '2018-01-15'), -- 26
(7, '2020-02-29'), -- 27
(8, '2014-08-01'), -- 28
(9, '2000-05-12'), -- 29
(10, '2013-11-25'), -- 30
(11, '2007-07-07'), -- 31
(19, '2020-10-10');

-- initial users
INSERT INTO account (fname, lname, phone, pin, username) VALUES ('John', 'Doe', 1234567890, 1234, 'johndoe'),
('Jane', 'Doe', 9876543210, 5678, 'janedoe'),
('Bob', 'Smith', 5555555555, 2468, 'bobsmith');

-- holds
INSERT INTO holds (book_id, account_id, time_placed) 
VALUES 
(1, 1, '2018-06-24 15:30:45'), 
(2, 1, '2019-08-12 11:20:10'), 
(3, 1, '2017-05-07 19:45:30'), 
(4, 2, '2017-05-07 19:45:30'), 
(5, 2, '2016-12-31 23:59:59'), 
(6, 2, '2022-01-01 12:00:00'), 
(7, 2, '2018-11-22 06:30:15'), 
(8, 3, '2021-07-15 14:10:25'), 
(9, 3, '2017-08-04 09:45:30'), 
(10, 3, '2023-02-14 18:20:40');

-- borrowing history
INSERT INTO borrowing_history(book_copy_id, account_id, date_checked_out, date_returned) 
VALUES
(1,1,'2014-08-01', '2014-09-21'), -- overdue
(5, 1, '2022-04-20', '2022-04-30'),
(14, 2, '2021-07-12', '2021-07-19'),
(23, 3, '2019-11-05', '2019-11-11'),
(10, 1, '2020-08-22', '2020-08-30'),
(8, 2, '2018-05-15', '2018-05-21'),
(16, 3, '2023-01-01', NULL), -- not returned yet
(31, 1, '2017-02-14', '2017-02-21'),
(3, 2, '2018-11-10', '2018-11-18'),
(29, 3, '2022-09-08', NULL), -- not returned yet
(11, 1, '2016-12-01', '2017-01-27'), -- overdue
(17, 2, '2020-03-20', '2020-03-28'),
(1, 3, '2023-04-01', NULL), -- not returned yet
(19, 1, '2017-06-10', '2017-06-16'),
(26, 2, '2019-02-14', '2019-02-22'),
(7, 3, '2021-10-12', NULL); -- not returned yet