# Task Manager API — Code Along

A minimal Spring Boot REST API project designed to teach core backend development concepts.

## What We're Building
A simple **Task Creation API** — one endpoint that accepts task data and saves it to a PostgreSQL database.

## Concepts Covered

| # | Concept | Where |
|---|---------|-------|
| 1 | Layered Architecture (Controller → Service → Repository → Database) | All files |
| 2 | Entity & JPA (mapping Java class to database table) | `Task.java` |
| 3 | Repository Pattern (free CRUD methods) | `TaskRepository.java` |
| 4 | Service Layer (business logic) | `TaskService.java` |
| 5 | REST Controller & HTTP Methods (POST) | `TaskController.java` |
| 6 | DTO Pattern (separating request/response from entity) | `TaskDto.java` |
| 7 | Validation (@NotBlank, @NotNull, @Valid) | `TaskDto.java`, `TaskController.java` |
| 8 | Builder Pattern | `Task.java`, `TaskService.java` |
| 9 | Dependency Injection | All classes using `@RequiredArgsConstructor` |
| 10 | Generics (`ApiResponse<T>`) | `ApiResponse.java` |
| 11 | Inheritance (BaseEntity) | `BaseEntity.java`, `Task.java` |
| 12 | Enums (fixed values) | `Task.java` |
| 13 | Auditing (auto timestamps) | `BaseEntity.java`, `JpaAuditingConfig.java` |

## Prerequisites
- Java 21
- PostgreSQL running on localhost:5432
- Database named `task` (create it: `CREATE DATABASE task;`)

## How to Run
```bash
./mvnw spring-boot:run
```

## Test the API
- Swagger UI: http://localhost:3031/swagger-ui.html
- POST `http://localhost:3031/api/v1/tasks`

### Sample Request Body:
```json
{
  "title": "Learn Spring Boot",
  "description": "Build a REST API from scratch",
  "dueDate": "2025-03-01",
  "priority": "HIGH"
}
```

### Sample Response:
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

## Project Structure
```
src/main/java/nene/backend/
├── NeneApplication.java          ← App entry point
├── common/
│   ├── config/
│   │   └── JpaAuditingConfig.java  ← Enables auto timestamps
│   ├── model/
│   │   └── BaseEntity.java         ← Shared fields (id, timestamps)
│   └── response/
│       └── ApiResponse.java        ← Standard API response wrapper
└── task/
    ├── controller/
    │   └── TaskController.java     ← HTTP endpoint (POST /api/v1/tasks)
    ├── dto/
    │   └── TaskDto.java            ← Request & Response shapes
    ├── model/
    │   └── Task.java               ← Database entity
    ├── repository/
    │   └── TaskRepository.java     ← Database operations
    └── service/
        └── TaskService.java        ← Business logic
```
