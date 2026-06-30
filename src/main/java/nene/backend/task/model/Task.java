package nene.backend.task.model;

import jakarta.persistence.*;
import lombok.*;
import nene.backend.common.model.BaseEntity;

import java.time.LocalDate;

// TODO: Concept - ENTITY (the Model/Data layer)
//  An @Entity class maps directly to a DATABASE TABLE.
//  Each instance of this class = one ROW in the "tasks" table.
//  Each field = one COLUMN in that table.

// TODO: Concept - LAYERED ARCHITECTURE
//  In Spring Boot, we separate concerns into layers:
//  1. MODEL (Entity)     - defines the data structure (this file)
//  2. REPOSITORY         - handles database queries
//  3. SERVICE            - contains business logic
//  4. CONTROLLER         - handles HTTP requests

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task extends BaseEntity {

    // TODO: Concept - COLUMN MAPPING
    //  @Column defines how this field maps to the database column.
    //  nullable = false means this field is REQUIRED (cannot be empty).
    @Column(name = "title", nullable = false)
    private String title;

    // TODO: Concept - TEXT vs VARCHAR
    //  columnDefinition = "TEXT" means unlimited length text in the database.
    //  Without it, String defaults to VARCHAR(255) - max 255 characters.
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // TODO: Concept - ENUM IN DATABASE
    //  @Enumerated(EnumType.STRING) stores the enum VALUE NAME (e.g., "HIGH")
    //  instead of a number. This makes the database readable.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private TaskStatus status = TaskStatus.CREATED;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    @Builder.Default
    private Priority priority = Priority.MEDIUM;

    @Column(name = "due_date")
    private LocalDate dueDate;

    // TODO: Concept - ENUM (a fixed set of allowed values)
    //  Instead of storing status as any random string, we define
    //  the ONLY allowed values here. This prevents invalid data.
    public enum TaskStatus {
        CREATED, IN_PROGRESS, DONE
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }
}
