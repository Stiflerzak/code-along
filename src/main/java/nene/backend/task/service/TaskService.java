package nene.backend.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nene.backend.task.dto.TaskDto;
import nene.backend.task.model.Task;
import nene.backend.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────

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

        return mapToResponse(savedTask);
    }

    // ─────────────────────────────────────────────
    // READ ALL
    // ─────────────────────────────────────────────

    // TODO: Concept - RETRIEVING ALL RECORDS
    //  taskRepository.findAll() does: SELECT * FROM tasks
    //  We then convert each Task entity → Response DTO using .map()
    public List<TaskDto.Response> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        log.info("Retrieved {} tasks", tasks.size());
        return tasks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ─────────────────────────────────────────────
    // READ ONE
    // ─────────────────────────────────────────────

    // TODO: Concept - FINDING BY ID
    //  findById() returns an Optional<Task> — it might be empty if the ID doesn't exist.
    //  .orElseThrow() unwraps the value OR throws an exception if not found.
    public TaskDto.Response getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return mapToResponse(task);
    }

    // ─────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────

    // TODO: Concept - UPDATING A RECORD
    //  1. Find the existing task (throw error if not found)
    //  2. Update only the fields that were sent
    //  3. Save it again — JPA knows to do an UPDATE (not INSERT) because the entity has an ID
    public TaskDto.Response updateTask(UUID id, TaskDto.UpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        if (request.getPriority() != null) {
            task.setPriority(Task.Priority.valueOf(request.getPriority()));
        }

        if (request.getStatus() != null) {
            task.setStatus(Task.TaskStatus.valueOf(request.getStatus()));
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully: {}", updatedTask.getTitle());

        return mapToResponse(updatedTask);
    }

    // ─────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────

    // TODO: Concept - DELETING A RECORD
    //  1. Check if the task exists first (throw error if not)
    //  2. Delete it by ID
    //  deleteById() does: DELETE FROM tasks WHERE id = ?
    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        taskRepository.deleteById(id);
        log.info("Task deleted successfully: {}", task.getTitle());
    }

    // ─────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────

    // TODO: Concept - PRIVATE HELPER METHOD
    //  Private means only THIS class can use it. It's a reusable utility.
    //  We use it in every method to convert Entity → Response DTO.
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
