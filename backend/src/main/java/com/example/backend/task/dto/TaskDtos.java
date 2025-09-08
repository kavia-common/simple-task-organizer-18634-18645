package com.example.backend.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/**
 * DTOs for Task create/update operations.
 */
public class TaskDtos {

    public static class CreateTaskRequest {
        @Schema(description = "Title of the task", example = "Buy groceries", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 200)
        public String title;

        @Schema(description = "Detailed description", example = "Milk, Bread, Eggs")
        @Size(max = 2000)
        public String description;

        @Schema(description = "Due date in ISO-8601 UTC", example = "2025-01-01T10:00:00Z")
        public Instant dueDate;
    }

    public static class UpdateTaskRequest {
        @Schema(description = "Title of the task", example = "Buy groceries (updated)")
        @Size(max = 200)
        public String title;

        @Schema(description = "Detailed description", example = "Milk, Bread, Eggs, Cheese")
        @Size(max = 2000)
        public String description;

        @Schema(description = "Completed status", example = "true")
        public Boolean completed;

        @Schema(description = "Due date in ISO-8601 UTC", example = "2025-01-01T10:00:00Z")
        public Instant dueDate;
    }

    public static class TaskResponse {
        public String id;
        public String userId;
        public String title;
        public String description;
        public Boolean completed;
        public Instant createdAt;
        public Instant updatedAt;
        public Instant dueDate;
    }
}
