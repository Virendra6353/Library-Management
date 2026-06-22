# Library Management System

A full-stack library administration application built with Spring Boot and PostgreSQL. It provides a browser-based dashboard for managing students, books, genres, and borrowing records, backed by a REST API.

## Features

- Dashboard with student, book, active borrowing, and daily return statistics
- Create, view, update, search, and delete students
- Create, view, update, search, and delete books and genres
- Issue books to students and record returns
- Browse borrowing history by student or book
- Prevent a book from being issued while it is already borrowed
- Prevent a student from holding more than one active book
- Responsive admin interface built with plain HTML, CSS, and JavaScript

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven Wrapper
- HTML, CSS, and vanilla JavaScript

## Project Structure

```text
src/
├── main/
│   ├── java/com/satoru/store/
│   │   ├── controller/    # REST endpoints
│   │   ├── model/         # JPA entities
│   │   ├── repository/    # Database repositories
│   │   └── services/      # Application business logic
│   └── resources/
│       ├── static/        # Dashboard UI
│       └── application.properties
└── test/                  # Application tests
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21
- PostgreSQL

Maven does not need to be installed separately because the project includes the Maven Wrapper.

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd store
```

### 2. Create the database

Open PostgreSQL and create a database for the application:

```sql
CREATE DATABASE sandbox;
```

### 3. Configure PostgreSQL

Update `src/main/resources/application.properties` with your local database details:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sandbox
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

For a public repository, do not commit real database credentials. Prefer environment variables or a local configuration file excluded by Git.

### 4. Run the application

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) in your browser.

Hibernate creates or updates the required tables automatically when the application starts.

## Using the Application

Create data in this order:

1. Add a genre.
2. Add a book and assign its genre.
3. Add a student.
4. Open **Borrow Records** and issue the book to the student.
5. Use **Return** when the book is brought back.

The search box filters the records displayed on the current page.

## REST API

All endpoints use the `/api/v1` prefix.

### Students

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/v1/student` | List all students |
| `GET` | `/api/v1/student/{id}` | Get a student by ID |
| `GET` | `/api/v1/student/email/{email}` | Get a student by email |
| `GET` | `/api/v1/student/name/{firstName}` | Find students by first name |
| `POST` | `/api/v1/student` | Create a student |
| `PUT` | `/api/v1/student/{id}` | Update a student |
| `DELETE` | `/api/v1/student/{id}` | Delete a student |

Example request body:

```json
{
  "firstName": "Aarav",
  "lastName": "Sharma",
  "email": "aarav@example.com",
  "age": 20
}
```

### Genres

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/v1/genre` | List all genres |
| `GET` | `/api/v1/genre/{id}` | Get a genre by ID |
| `GET` | `/api/v1/genre/name/{name}` | Get a genre by name |
| `POST` | `/api/v1/genre` | Create a genre |
| `PUT` | `/api/v1/genre/{id}` | Update a genre |
| `DELETE` | `/api/v1/genre/{id}` | Delete a genre |

Example request body:

```json
{
  "name": "Science Fiction"
}
```

### Books

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/v1/book` | List all books |
| `GET` | `/api/v1/book/{id}` | Get a book by ID |
| `GET` | `/api/v1/book/isbn/{isbn}` | Get a book by ISBN |
| `GET` | `/api/v1/book/bookName/{bookName}` | Find books by name |
| `GET` | `/api/v1/book/author/{author}` | Find books by author |
| `GET` | `/api/v1/book/genre/{id}` | List books in a genre |
| `POST` | `/api/v1/book` | Create a book |
| `PUT` | `/api/v1/book/{id}` | Update a book |
| `DELETE` | `/api/v1/book/{id}` | Delete a book |

The genre must already exist. Example request body:

```json
{
  "bookName": "Dune",
  "author": "Frank Herbert",
  "isbn": "9780441172719",
  "genre": {
    "id": 1,
    "name": "Science Fiction"
  }
}
```

### Borrowing Records

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/v1/borrowBook` | List all borrowing records |
| `GET` | `/api/v1/borrowBook/{id}` | Get a borrowing record by ID |
| `GET` | `/api/v1/borrowBook/student/{studentId}` | List a student's borrowing history |
| `GET` | `/api/v1/borrowBook/book/{bookId}` | List a book's borrowing history |
| `POST` | `/api/v1/borrowBook?studentId={studentId}&bookId={bookId}` | Issue a book |
| `PUT` | `/api/v1/borrowBook/returnbook/{id}` | Mark a borrowing as returned |

## Testing

Run the test suite with:

```bash
./mvnw test
```

## Build

Create an executable JAR with:

```bash
./mvnw clean package
java -jar target/Library-0.0.1-SNAPSHOT.jar
```
