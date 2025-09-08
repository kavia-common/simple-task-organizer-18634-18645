package com.example.backend.task;

import com.example.backend.task.dto.TaskDtos.CreateTaskRequest;
import com.example.backend.task.dto.TaskDtos.TaskResponse;
import com.example.backend.task.dto.TaskDtos.UpdateTaskRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service layer for Tasks.
 */
@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    // PUBLIC_INTERFACE
    public TaskResponse create(String userId, CreateTaskRequest req) {
        Task task = new Task(userId, req.title, req.description, false, req.dueDate);
        Task saved = repo.save(task);
        return toResponse(saved);
    }

    // PUBLIC_INTERFACE
    public List<TaskResponse> list(String userId) {
        return repo.findByUserId(userId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    // PUBLIC_INTERFACE
    public Optional<TaskResponse> get(String userId, String id) {
        return repo.findById(id)
                .filter(t -> t.getUserId().equals(userId))
                .map(this::toResponse);
    }

    // PUBLIC_INTERFACE
    public Optional<TaskResponse> update(String userId, String id, UpdateTaskRequest req) {
        return repo.findById(id)
                .filter(t -> t.getUserId().equals(userId))
                .map(t -> {
                    if (req.title != null) t.setTitle(req.title);
                    if (req.description != null) t.setDescription(req.description);
                    if (req.completed != null) t.setCompleted(req.completed);
                    if (req.dueDate != null) t.setDueDate(req.dueDate);
                    t.touch();
                    return repo.save(t);
                })
                .map(this::toResponse);
    }

    // PUBLIC_INTERFACE
    public boolean delete(String userId, String id) {
        return repo.findById(id)
                .filter(t -> t.getUserId().equals(userId))
                .map(t -> {
                    repo.deleteById(id);
                    return true;
                }).orElse(false);
    }

    private TaskResponse toResponse(Task t) {
        TaskResponse r = new TaskResponse();
        r.id = t.getId();
        r.userId = t.getUserId();
        r.title = t.getTitle();
        r.description = t.getDescription();
        r.completed = t.getCompleted();
        r.createdAt = t.getCreatedAt();
        r.updatedAt = t.getUpdatedAt();
        r.dueDate = t.getDueDate();
        return r;
    }
}
