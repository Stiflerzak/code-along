# 🧑‍💻 Code Along Guide — Task Manager API

---

## Step 1: Create the Project (Spring Initializr)

Go to: **https://start.spring.io**

### Settings to select:

| Setting | Value |
|---------|-------|
| Project | Maven |
| Language | Java |
| Spring Boot | 4.0.2 (latest stable) |
| Group | `nene` |
| Artifact | `nene-backend` |
| Name | `nene-backend` |
| Packaging | Jar |
| Java | 21 |

### Dependencies to add:

| Dependency | Why we need it |
|------------|---------------|
| **Spring Web** | Lets us create REST APIs — handles HTTP requests (GET, POST, PUT, DELETE) and returns JSON responses |
| **Spring Data JPA** | Connects our Java classes to database tables. Gives us free CRUD methods (save, find, delete) without writing SQL |
| **PostgreSQL Driver** | The "translator" between Java and PostgreSQL database. Without it, Java can't talk to Postgres |
| **Validation** | Lets us validate incoming data with simple annotations like `@NotBlank`, `@NotNull`. Rejects bad requests automatically |
| **Lombok** | Eliminates boilerplate code. Generates getters, setters, constructors, builders at compile time using annotations |
| **Spring Boot DevTools** | Auto-restarts the server when you save a file. Speeds up development (no manual restarts!) |

> 💡 **Click "Generate"** → Download the ZIP → Extract → Open in IntelliJ

---

## Step 2: Add Swagger (manual dependency)

Swagger gives us a beautiful UI to test our API without Postman.

Open `pom.xml` and add this inside `<dependencies>`:

```xml
<!-- Swagger - API Documentation & Testing UI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```

> 💡 After adding, the Swagger UI will be available at: http://localhost:3031/swagger-ui.html

---

## Step 3: Understand What We're Building

### The Big Picture

Think of a **restaurant**:
- **Customer** (Client/Frontend) → places an order
- **Waiter** (Controller) → takes the order, delivers food back
- **Chef** (Service) → prepares the food (business logic)
- **Pantry** (Repository) → stores and retrieves ingredients (data)
- **Fridge** (Database) → where everything is actually kept

In code:
```
Client (Postman/Frontend)
    ↓ sends JSON
Controller (receives HTTP request)
    ↓ passes data
Service (processes business logic)
    ↓ passes entity
Repository (saves/retrieves from DB)
    ↓ SQL query
Database (PostgreSQL)
```

### What's our one endpoint?

```
POST /api/v1/tasks → Creates a new task and saves it to the database
```

---

## Step 4: Plan Our Custom Response

### ❌ Without a standard response (inconsistent, messy):
```json
// Success:
{ "id": "abc", "title": "Do something" }

// Error:
{ "error": "Title is required" }

// Another error:
"Something went wrong"
```

### ✅ With a standard response (consistent, predictable):
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": { ... },
  "timestamp": "2025-01-15T10:30:00"
}
```

### Why? — The Frontend Developer's Best Friend

A standard response means:
1. Frontend ALWAYS knows the shape of the response
2. They can check `success: true/false` to decide what to show
3. `message` can be displayed as a toast/notification
4. `data` contains the actual payload
5. `timestamp` tells when the response was generated

### The Plan:

```java
ApiResponse<T>
├── success   → boolean (did it work?)
├── message   → String (human-readable message)
├── data      → T (any type — this is where Generics shine)
└── timestamp → LocalDateTime (when it happened)
```

> 💡 **Generics (`<T>`)** = "This can hold ANY type"
> - `ApiResponse<Task>` → data is a Task
> - `ApiResponse<String>` → data is a String
> - `ApiResponse<List<Task>>` → data is a list of Tasks

---

## Step 5: Database Configuration

### 5.1 — Create the database in PostgreSQL

Open your terminal or pgAdmin and run:

```sql
CREATE DATABASE task;
```

### 5.2 — Configure the connection

Open `src/main/resources/application.yaml` and set up:

```yaml
spring:
  application:
    name: task-manager

  # DATABASE CONNECTION
  # Think of this as giving Spring the "address" and "keys" to your database
  datasource:
    url: jdbc:postgresql://localhost:5432/task    # where is the database?
    username: your_username                       # who are you?
    password: your_password                       # prove it
    driver-class-name: org.postgresql.Driver      # which "translator" to use?

  # JPA/HIBERNATE SETTINGS
  # Hibernate is the ORM — it converts Java objects ↔ database rows
  jpa:
    hibernate:
      ddl-auto: update    # auto-create/update tables from your Java classes
    show-sql: true        # print SQL queries to console (great for learning!)
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect   # speak PostgreSQL
        format_sql: true                                   # pretty-print the SQL

# SERVER PORT
server:
  port: 3031

# SWAGGER CONFIG
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
```

### 5.3 — Understanding `ddl-auto` options:

| Value | What it does | When to use |
|-------|-------------|-------------|
| `update` | Creates new tables/columns, never drops existing ones | **Development** (we use this) |
| `create` | Drops ALL tables and recreates on every startup | Testing (dangerous!) |
| `create-drop` | Same as create, but also drops on shutdown | Unit tests only |
| `validate` | Only checks that tables match entities, never changes DB | **Production** |
| `none` | Does nothing | Production with manual migrations |

---

## Step 6: How a Java Class Maps to a Database Table

This is the **magic of JPA (Java Persistence API)**:

```
Java World                          Database World
─────────────                       ──────────────
@Entity class Task          →       CREATE TABLE tasks
    field title             →           column "title"
    field description       →           column "description"
    field status            →           column "status"
    @Id UUID id             →           PRIMARY KEY "id"
```

### The mapping rules:

| Java | Database |
|------|----------|
| Class annotated with `@Entity` | Table |
| Field | Column |
| Field annotated with `@Id` | Primary Key |
| Instance (object) | Row |
| `Task.builder().title("X").build()` | One row with title = 'X' |

### Visual example:

```java
@Entity
@Table(name = "tasks")       // ← table name in the database
public class Task {
    @Id
    private UUID id;          // ← PRIMARY KEY column

    @Column(name = "title")
    private String title;     // ← "title" column, VARCHAR(255)

    @Column(columnDefinition = "TEXT")
    private String description; // ← "description" column, unlimited text

    @Enumerated(EnumType.STRING)
    private TaskStatus status;  // ← "status" column, stored as text "CREATED"
}
```

Becomes this table:

```
┌──────────────────────────────────────────────────────────────┐
│                        tasks                                  │
├──────────────┬──────────┬─────────────────────┬──────────────┤
│ id (UUID)    │ title    │ description         │ status       │
├──────────────┼──────────┼─────────────────────┼──────────────┤
│ a1b2c3d4-... │ Fix bug  │ Login page broken   │ CREATED      │
│ e5f6g7h8-... │ Add docs │ Write API docs      │ IN_PROGRESS  │
└──────────────┴──────────┴─────────────────────┴──────────────┘
```

---

## Step 7: How Repository Connects to the Entity

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> { }
//                                                      ↑      ↑
//                                               Entity type  ID type
```

By just writing this ONE line, Spring gives you:

| Method | What it does | SQL equivalent |
|--------|-------------|----------------|
| `save(task)` | Insert or update | `INSERT INTO tasks ...` |
| `findById(id)` | Find one by ID | `SELECT * FROM tasks WHERE id = ?` |
| `findAll()` | Get all records | `SELECT * FROM tasks` |
| `deleteById(id)` | Delete one | `DELETE FROM tasks WHERE id = ?` |
| `count()` | Count records | `SELECT COUNT(*) FROM tasks` |

**Zero SQL written. Zero implementation code. Just the interface.**

---

## Step 8: The Full Data Flow (How It All Connects)

```
1. Client sends POST /api/v1/tasks with JSON body:
   { "title": "Learn Spring Boot", "dueDate": "2025-03-01", "priority": "HIGH" }

2. TaskController receives it:
   - @RequestBody converts JSON → CreateRequest object
   - @Valid checks: is title blank? is dueDate null?

3. TaskService processes it:
   - Converts CreateRequest → Task entity (using Builder)
   - Calls taskRepository.save(task)

4. TaskRepository saves it:
   - JPA converts Task object → SQL INSERT
   - PostgreSQL stores the row and generates the UUID

5. Response flows back:
   - Repository returns saved Task (with id + timestamps)
   - Service converts Task → Response DTO
   - Controller wraps in ApiResponse and returns 201 CREATED
```

---

## Step 9: File Creation Order (What We'll Code)

Follow this order — each step builds on the previous:

| # | File | Why this order? |
|---|------|----------------|
| 1 | `application.yaml` | Set up the database connection first |
| 2 | `BaseEntity.java` | Shared fields (id, timestamps) — other entities depend on this |
| 3 | `Task.java` | Define what data we're storing |
| 4 | `TaskRepository.java` | Give us database access (1 line!) |
| 5 | `TaskDto.java` | Define request/response shapes |
| 6 | `ApiResponse.java` | Standard response wrapper |
| 7 | `TaskService.java` | Business logic connecting everything |
| 8 | `TaskController.java` | The HTTP endpoint (the door to our API) |
| 9 | `JpaAuditingConfig.java` | Turn on auto-timestamps |

---

## Step 10: Test It! 🎉

### Option A: Swagger UI
1. Run the app: `./mvnw spring-boot:run`
2. Open: http://localhost:3031/swagger-ui.html
3. Find POST `/api/v1/tasks`
4. Click "Try it out"
5. Paste the request body and execute

### Option B: cURL
```bash
curl -X POST http://localhost:3031/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Build a REST API from scratch",
    "dueDate": "2025-03-01",
    "priority": "HIGH"
  }'
```

### Expected response:
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": "a1b2c3d4-...",
    "title": "Learn Spring Boot",
    "description": "Build a REST API from scratch",
    "status": "CREATED",
    "priority": "HIGH",
    "dueDate": "2025-03-01",
    "createdAt": "2025-01-15T10:30:00",
    "updatedAt": "2025-01-15T10:30:00"
  },
  "timestamp": "2025-01-15T10:30:00"
}
```

### Test validation (send empty title):
```bash
curl -X POST http://localhost:3031/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{ "title": "", "dueDate": "2025-03-01" }'
```

Should return **400 Bad Request** with "Title is required" error.

---

## Quick Reference: Annotations Cheat Sheet

| Annotation | Meaning |
|-----------|---------|
| `@SpringBootApplication` | "This is the starting point of my app" |
| `@Entity` | "This class = a database table" |
| `@Table(name = "tasks")` | "The table is called 'tasks'" |
| `@Id` | "This field is the primary key" |
| `@Column` | "This field is a database column" |
| `@Repository` | "This handles database operations" |
| `@Service` | "This contains business logic" |
| `@RestController` | "This handles HTTP requests and returns JSON" |
| `@RequestMapping` | "Base URL for all endpoints here" |
| `@PostMapping` | "This method handles POST requests" |
| `@RequestBody` | "Convert the JSON body into this Java object" |
| `@Valid` | "Validate this object before using it" |
| `@NotBlank` | "This field can't be empty" |
| `@NotNull` | "This field can't be null" |
| `@RequiredArgsConstructor` | "Generate a constructor for final fields (DI)" |
| `@Builder` | "Generate a builder pattern for this class" |
| `@Data` | "Generate getters, setters, toString, equals, hashCode" |
| `@Configuration` | "This class sets up app configuration" |
| `@EnableJpaAuditing` | "Turn on auto timestamps" |
