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
- see [project PDF](https://github.com/audreytracy/library-app/blob/master/CSCI%20366%20Final%20Project.pdf) for an overview of the database design and implementation

<a id="top"></a>

# Estimation Assistant Web Application

<div align="center">
    <h2 align="center">Library Database Management Application</h2>
    <p align="center"> Final project for CSCI 366 - Database Systems at North Dakota State University </p>
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#overview">Overview</a>
      <ul>
        <li><a href="#purpose">Purpose</a></li>
        <li><a href="#product-description">Product Description</a></li>
        <li><a href="#built-with">Built With</a></li>
        <li><a href="#features">Features</a></li>
      </ul>
    </li>
    <li><a href="#setup">Setup</a></li>
    <ul>
        <li><a href="#purpose">Prereqs</a></li>
        <li><a href="#setup-steps">Setup Steps</a></li>
      </ul>
    <li><a href="#database-design">Database Design</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributors">Contributors</a></li>
  </ol>
</details>


## Overview

### Purpose

This project was the final project for my database class. A large part of the project was database design & SQL implementation of this design. After the 

### Product Description

This application provides a user interface for manipulating a SQL database. 

### Built With

* [![NetBeans IDE][NetBeans]][NetBeans-url]
* [![PostgreSQL][PostgreSQL]][PostgreSQL-url]
* [![Java][Java]][Java-url]

### Features

Filter search results by author last name, book title, or genre.  

![image](https://github.com/user-attachments/assets/922e0b89-447c-4003-935c-a7eb412a8d65)

View book summary & see option to place hold or checkout book:  

![image](https://github.com/user-attachments/assets/3dbdfb10-71c4-4c56-9755-76be6a23e572)



## Setup

### Prerequisites

- PostgreSQL Server  
- Java  
- NetBeans IDE  
- JDBC bin file added to classpath  

### Setup Steps

- Clone this repository locally using 
    ```sh
    https://github.com/audreytracy/library-app.git
    ```
- Open in NetBeans
- Add your database login credentials to [SQLQueries.java](https://github.com/audreytracy/library-app/blob/master/src/SQLQueries.java) on lines [25 (username)](https://github.com/audreytracy/library-app/blob/f55d9b0a9c300aae97aafc42cd1c1fa33dcedc2d/src/SQLQueries.java#L25) and [26 (password)](https://github.com/audreytracy/library-app/blob/f55d9b0a9c300aae97aafc42cd1c1fa33dcedc2d/src/SQLQueries.java#L26)
- Run  

## Database Design

Entity Relationship Diagram  
  
![download (1)](https://github.com/user-attachments/assets/fe192ef7-f48a-4a4e-a7c4-f07194d06c13)


## Roadmap

Some features that would be useful to add in the future:  

- [ ] Ability to create account from application
- [ ] Further modularization of code
- [ ] Addition of an admin account type
    - [ ] Ability for admin to add or remove books from system
- [ ] Password security was not the focus of this application, so passwords are stored as plaintext. That would be something to change in this app's future  


<p align="right">(<a href="#top">back to top</a>)</p>

[NetBeans]: https://img.shields.io/badge/NetBeans_IDE-a6073f?style=for-the-badge&logo=apachenetbeanside&logoColor=white
[NetBeans-url]: https://netbeans.apache.org/
[PostgreSQL]: https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/
[Java]: https://img.shields.io/badge/Java-3a75af?style=for-the-badge&logo=coffeescript&logoColor=white
[Java-url]: https://www.java.com

