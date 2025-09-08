package com.example.backend.task;

import java.time.Instant;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Task entity stored in MongoDB.
 */
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private String userId;
    private String title;
    private String description;
    private Boolean completed = false;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Instant dueDate;

    public Task() {}

    public Task(String userId, String title, String description, Boolean completed, Instant dueDate) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.completed = completed != null ? completed : false;
        this.dueDate = dueDate;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Boolean getCompleted() { return completed; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getDueDate() { return dueDate; }

    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }

    public void touch() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
