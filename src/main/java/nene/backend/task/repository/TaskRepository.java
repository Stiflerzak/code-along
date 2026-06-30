package nene.backend.task.repository;

import nene.backend.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// TODO: Concept - REPOSITORY PATTERN
//  A Repository handles all DATABASE OPERATIONS (CRUD).
//  By extending JpaRepository, we get these methods FOR FREE:
//    - save(task)        → INSERT or UPDATE a task
//    - findById(id)      → SELECT a task by ID
//    - findAll()         → SELECT all tasks
//    - deleteById(id)    → DELETE a task by ID
//    - count()           → COUNT all tasks

// TODO: Concept - INTERFACE vs CLASS
//  This is an INTERFACE (not a class). We only DECLARE methods here.
//  Spring automatically creates the implementation at runtime — magic!
//  JpaRepository<Task, UUID> means:
//    - Task = the entity type we're working with
//    - UUID = the type of the entity's primary key (id field)

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    // TODO: Concept - DERIVED QUERY METHODS
    //  Spring can generate queries just from the METHOD NAME!
    //  "findByStatus" → SELECT * FROM tasks WHERE status = ?
    //  You don't write any SQL — Spring figures it out from the name.

    // We'll add custom queries here as needed
}
