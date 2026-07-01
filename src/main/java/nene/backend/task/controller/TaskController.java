package nene.backend.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nene.backend.common.response.ApiResponse;
import nene.backend.task.dto.TaskDto;
import nene.backend.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// TODO: Concept - CONTROLLER (the Entry Point)
//  A Controller handles incoming HTTP REQUESTS from clients.
//  It's the "front door" of your API.
//  Client (browser/postman) → Controller → Service → Repository → Database

// TODO: Concept - @RestController
//  Combines @Controller + @ResponseBody.
//  It tells Spring: "This class handles HTTP requests and returns JSON."

// TODO: Concept - @RequestMapping("/api/v1/tasks")
//  Sets the BASE URL for all endpoints in this controller.
//  All URLs here will start with: http://localhost:3031/api/v1/tasks

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
public class TaskController {

    private final TaskService taskService;

    // ─────────────────────────────────────────────
    // CREATE — POST /api/v1/tasks
    // ─────────────────────────────────────────────

    // TODO: Concept - HTTP POST = CREATE something new
    //  POST /api/v1/tasks → creates a new task
    //  The client sends task data in the REQUEST BODY (as JSON).

    // TODO: Concept - @Valid
    //  Triggers the validation annotations in CreateRequest (@NotBlank, @NotNull).
    //  If validation fails, Spring automatically returns 400 Bad Request.

    // TODO: Concept - @RequestBody
    //  Tells Spring: "Take the JSON from the request body and convert it
    //  into a CreateRequest object for me." (This is called DESERIALIZATION)

    // TODO: Concept - ResponseEntity
    //  Lets us control the full HTTP response: status code + headers + body.
    //  HttpStatus.CREATED (201) = "Resource was successfully created."
    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task and saves it to the database")
    public ResponseEntity<ApiResponse<TaskDto.Response>> createTask(
            @Valid @RequestBody TaskDto.CreateRequest request
    ) {
        TaskDto.Response response = taskService.createTask(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task created successfully", response));
    }

    // ─────────────────────────────────────────────
    // READ ALL — GET /api/v1/tasks
    // ─────────────────────────────────────────────

    // TODO: Concept - HTTP GET = RETRIEVE/READ data
    //  GET /api/v1/tasks → returns all tasks
    //  No request body needed — we're just asking for data.
    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieves all tasks from the database")
    public ResponseEntity<ApiResponse<List<TaskDto.Response>>> getAllTasks() {
        List<TaskDto.Response> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", tasks));
    }

    // ─────────────────────────────────────────────
    // READ ONE — GET /api/v1/tasks/{id}
    // ─────────────────────────────────────────────

    // TODO: Concept - PATH VARIABLE (@PathVariable)
    //  The {id} in the URL becomes a method parameter.
    //  Example: GET /api/v1/tasks/a1b2c3d4-... → id = "a1b2c3d4-..."
    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieves a single task by its UUID")
    public ResponseEntity<ApiResponse<TaskDto.Response>> getTaskById(@PathVariable UUID id) {
        TaskDto.Response response = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.success("Task retrieved successfully", response));
    }

    // ─────────────────────────────────────────────
    // UPDATE — PUT /api/v1/tasks/{id}
    // ─────────────────────────────────────────────

    // TODO: Concept - HTTP PUT = UPDATE an existing resource
    //  PUT /api/v1/tasks/{id} → updates the task with given ID.
    //  We need BOTH the ID (from URL) and the new data (from body).
    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Updates an existing task by its UUID")
    public ResponseEntity<ApiResponse<TaskDto.Response>> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody TaskDto.UpdateRequest request
    ) {
        TaskDto.Response response = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.success("Task updated successfully", response));
    }

    // ─────────────────────────────────────────────
    // DELETE — DELETE /api/v1/tasks/{id}
    // ─────────────────────────────────────────────

    // TODO: Concept - HTTP DELETE = REMOVE a resource
    //  DELETE /api/v1/tasks/{id} → deletes the task with given ID.
    //  Returns 200 OK with a confirmation message (no data body needed).
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a task by its UUID")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success("Task deleted successfully", null));
    }
}
