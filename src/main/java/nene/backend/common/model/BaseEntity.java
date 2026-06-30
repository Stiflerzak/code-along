package nene.backend.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

// TODO: Concept - INHERITANCE
//  BaseEntity is a parent class that other entities will EXTEND.
//  This avoids repeating id, createdAt, updatedAt in every entity.

// TODO: Concept - @MappedSuperclass
//  Tells JPA: "Don't create a table for this class, but include its fields
//  in the tables of classes that extend it."

// TODO: Concept - @EntityListeners(AuditingEntityListener.class)
//  Automatically fills in createdAt and updatedAt timestamps for us.

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    // TODO: Concept - PRIMARY KEY
    //  Every database table needs a unique identifier for each row.
    //  We use UUID (universally unique ID) instead of auto-increment numbers.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // TODO: Concept - AUDIT FIELDS
    //  These track WHEN a record was created and last updated.
    //  @CreatedDate and @LastModifiedDate are filled automatically by Spring.
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
