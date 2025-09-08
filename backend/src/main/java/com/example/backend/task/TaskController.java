package com.example.backend.task;

import com.example.backend.task.dto.TaskDtos.CreateTaskRequest;
import com.example.backend.task.dto.TaskDtos.TaskResponse;
import com.example.backend.task.dto.TaskDtos.UpdateTaskRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoints for Task CRUD operations.
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "CRUD operations for tasks. Requires authentication.")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    // PUBLIC_INTERFACE
    @PostMapping
    @Operation(summary = "Create task", description = "Create a new task for the authenticated user.")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest req, Authentication auth) {
        String userId = auth.getName();
        TaskResponse created = service.create(userId, req);
        return ResponseEntity.created(URI.create("/api/tasks/" + created.id)).body(created);
    }

    // PUBLIC_INTERFACE
    @GetMapping
    @Operation(summary = "List tasks", description = "List all tasks for the authenticated user.")
    public List<TaskResponse> list(Authentication auth) {
        String userId = auth.getName();
        return service.list(userId);
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{id}")
    @Operation(summary = "Get task", description = "Get a task by id belonging to the authenticated user.")
    public ResponseEntity<TaskResponse> get(@PathVariable String id, Authentication auth) {
        String userId = auth.getName();
        Optional<TaskResponse> resp = service.get(userId, id);
        return resp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUBLIC_INTERFACE
    @PatchMapping("/{id}")
    @Operation(summary = "Update task", description = "Partially update fields of the task.")
    public ResponseEntity<TaskResponse> update(@PathVariable String id, @Valid @RequestBody UpdateTaskRequest req, Authentication auth) {
        String userId = auth.getName();
        Optional<TaskResponse> resp = service.update(userId, id, req);
        return resp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUBLIC_INTERFACE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Delete a task belonging to the authenticated user.")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication auth) {
        String userId = auth.getName();
        boolean deleted = service.delete(userId, id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
