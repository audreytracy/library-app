# library-app
- [sql file](https://github.com/audreytracy/library-app/blob/master/src/sql/test.sql) used for setting up database.
- make sure you have PostgreSQL server installed on your device. Edit [SQLQueries.java](https://github.com/audreytracy/library-app/blob/master/src/SQLQueries.java) to set up your account's details (especially lines 25-26 where postrgres username & password are defined)
- initial users of app (currenly no ability to add users)
    ```
    INSERT INTO account (pin, username) VALUES
      (1234, 'johndoe'),
      (5678, 'janedoe'),
      (2468, 'bobsmith'),
      (1111, 'demoacct');
    ```
