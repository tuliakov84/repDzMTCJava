package com.mipt.sem2.hw4.api;

import com.mipt.sem2.hw4.dto.CreateTaskRequest;
import com.mipt.sem2.hw4.dto.TaskDto;
import com.mipt.sem2.hw4.service.TasksGatewayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TasksController {

    private final TasksGatewayService tasksGatewayService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskDto taskDto = TaskDto.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .completed(request.getCompleted())
            .build();
        TaskDto created = tasksGatewayService.createTask(taskDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        TaskDto task = tasksGatewayService.getTask(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(
        @RequestParam(required = false) Boolean completed,
        @RequestParam(required = false) Integer limit) {
        List<TaskDto> tasks = tasksGatewayService.getTasks(completed, limit);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        tasksGatewayService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}