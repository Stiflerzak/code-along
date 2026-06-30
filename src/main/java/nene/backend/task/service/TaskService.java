package nene.backend.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nene.backend.task.dto.TaskDto;
import nene.backend.task.model.Task;
import nene.backend.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

// TODO: Concept - SERVICE LAYER
//  The Service layer contains BUSINESS LOGIC — the "brain" of your app.
//  It sits between the Controller (HTTP) and Repository (Database).
//  Controller → Service → Repository → Database

// TODO: Concept - @Service ANNOTATION
//  Tells Spring: "This is a service class, manage it for me."
//  Spring creates ONE instance and injects it wherever needed.

// TODO: Concept - DEPENDENCY INJECTION (@RequiredArgsConstructor)
//  Instead of creating dependencies manually with `new TaskRepository()`,
//  Spring INJECTS them through the constructor automatically.
//  @RequiredArgsConstructor (Lombok) generates the constructor for us.

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // TODO: Concept - METHOD that handles CREATE logic
    //  1. Takes in a DTO (what the client sent)
    //  2. Converts it to an Entity (what the database understands)
    //  3. Saves it to the database
    //  4. Converts the saved entity back to a Response DTO
    //  5. Returns it to the controller
    public TaskDto.Response createTask(TaskDto.CreateRequest request) {

        // TODO: Concept - BUILDER PATTERN
        //  Instead of: new Task() → task.setTitle() → task.setDescription()...
        //  We use: Task.builder().title(...).description(...).build()
        //  It's cleaner and more readable.
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .priority(request.getPriority() != null
                        ? Task.Priority.valueOf(request.getPriority())
                        : Task.Priority.MEDIUM)
                .build();

        // TODO: Concept - SAVING TO DATABASE
        //  taskRepository.save() does an INSERT into the database.
        //  It returns the saved entity WITH the auto-generated id and timestamps.
        Task savedTask = taskRepository.save(task);

        // TODO: Concept - LOGGING
        //  log.info() prints a message to the console for debugging.
        //  {} is a placeholder that gets replaced by the value after the comma.
        log.info("Task created successfully: {}", savedTask.getTitle());

        // TODO: Concept - MAPPING ENTITY → RESPONSE DTO
        //  We convert the entity to a response DTO before sending it back.
        //  This controls exactly what the client sees.
        return mapToResponse(savedTask);
    }

    // TODO: Concept - PRIVATE HELPER METHOD
    //  Private means only THIS class can use it. It's a reusable utility.
    private TaskDto.Response mapToResponse(Task task) {
        return TaskDto.Response.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .priority(task.getPriority().name())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
