package com.mipt.sem2.todolist.controller;

import com.mipt.sem2.todolist.model.Task;
import com.mipt.sem2.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<Task> getAllTasks() {
    return taskService.getAllTasks();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
    return taskService.getTaskById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Task> createTask(@RequestBody Task task) {
    Task created = taskService.createTask(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(@PathVariable UUID id, @RequestBody Task task) {
    return taskService.updateTask(id, task)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
    if (taskService.deleteTask(id)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}