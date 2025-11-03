#  Library Management System

This is a simple **Library Management System** built using **Spring Boot, JPA, H2 Database**, and **Criteria API**.  
I built this project to practice backend development concepts like REST APIs, entity relationships, exception handling, and query building using Criteria API.

---

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/ashishkiran93/library-management-system.git
cd library-management-system
```
### 2. Open in IDE

Open the project in your favorite IDE — I used IntelliJ IDEA, but Eclipse or VS Code will also work.

### 3. Build the Project

Since I used Gradle, just refresh the Gradle project and make sure all dependencies are downloaded

### 4. Run the Application

Simply run the main class:

LibraryManagementSystemApplication.java

Once it starts, the app runs at:

http://localhost:8080


## H2 Database Setup

The project uses an in-memory H2 database for simplicity.
You can access the database console here:
```bash
http://localhost:8080/h2-console
```
Use the following credentials:

JDBC URL: jdbc:h2:mem:librarydb

Username: sa

Password: sa

(Make sure these match your application.properties)

## Tech Stack
| Layer             | Technology      |
| ----------------- | --------------- |
| Backend Framework | Spring Boot     |
| ORM               | Spring Data JPA |
| Database          | H2 (in-memory)  |
| Querying          | Criteria API    |
| Build Tool        | Gradle          |
| Language          | Java 17+        |


## Features Implemented
* Add, update, and delete Books
* Manage Borrowers and their borrowed records
* Track available copies and book availability
* Handle due dates and overdue calculations
* Implement custom queries using JPA Criteria API
* Global exception handling with @RestControllerAdvice

## My Approach & Thought Process
When I started this project, my goal was to simulate how a real-world library system might manage its data — but without overcomplicating it.
The Criteria API part was interesting — I wanted to go beyond basic JPA methods and learn how to dynamically build queries programmatically.

For clean design, I separated the project into layers:
Controller: handles HTTP requests
Service: contains business logic
Repository: talks to the database
Model: contains entity classes
I also used a GlobalExceptionHandler to handle cases like missing books or borrowers gracefully — something I’ve seen done in production-grade Spring Boot apps.

## Challenges & How I Solved Them
Dynamic Queries with Criteria API

At first, the Criteria API looked verbose and intimidating.
To simplify, I broke queries into smaller reusable predicates and used helper methods to keep the service layer readable

## Testing the APIs

Once the app is running, you can use Postman or curl to test endpoints like:

POST /books
GET /books
GET /books/{id}
PUT /book/{id}
DELETE /book/{id}
POST /borrowers
GET /borrowers/{id}
POST /borrow
GET /borrow/overdue
POST /return/borrowerId/bookId
GET /records/active