package nene.backend.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nene.backend.common.response.ApiResponse;
import nene.backend.task.dto.TaskDto;
import nene.backend.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class TaskController {

    private final TaskService taskService;

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
    public ResponseEntity<ApiResponse<TaskDto.Response>> createTask(
            @Valid @RequestBody TaskDto.CreateRequest request
    ) {
        TaskDto.Response response = taskService.createTask(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task created successfully", response));
    }
}
