# Notes App — REST API

A full-stack notes application backend built with Spring Boot.

## Tech Stack
- Java 21
- Spring Boot 3.5
- Spring Data JPA
- H2 Database
- Maven
- Lombok

## Features
- Create, read, update, delete notes
- Live search by title or content
- Persistent file-based H2 database
- RESTful API design with proper HTTP status codes

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/notes | Get all notes |
| GET | /api/notes/{id} | Get note by ID |
| POST | /api/notes | Create a note |
| PUT | /api/notes/{id} | Update a note |
| DELETE | /api/notes/{id} | Delete a note |
| GET | /api/notes/search?keyword= | Search notes |

## How to Run
1. Clone the repo
2. Open in IntelliJ
3. Run UrlshortenerApplication.java
4. API available at http://localhost:8080

## Frontend
React frontend repo: https://github.com/Vansika-Singh/notes-ui
