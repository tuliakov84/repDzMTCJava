package com.mipt.sem2.hw1.todolist.controller;

import com.mipt.sem2.hw1.todolist.dto.TaskCreateDto;
import com.mipt.sem2.hw1.todolist.dto.TaskResponseDto;
import com.mipt.sem2.hw1.todolist.dto.TaskUpdateDto;
import com.mipt.sem2.hw1.todolist.mapper.TaskMapper;
import com.mipt.sem2.hw1.todolist.model.Task;
import com.mipt.sem2.hw1.todolist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @GetMapping
  public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
    List<TaskResponseDto> tasks = taskService.getAllTasks().stream()
        .map(taskMapper::toResponseDto)
        .collect(Collectors.toList());
    return ResponseEntity.ok(tasks);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID id) {
    return taskService.getTaskById(id)
        .map(taskMapper::toResponseDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskCreateDto dto) {
    Task task = taskMapper.toEntity(dto);
    Task created = taskService.createTask(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toResponseDto(created));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskResponseDto> updateTask(@PathVariable UUID id,
                                                    @RequestBody TaskUpdateDto dto) {
    return taskService.updateTask(id, existing -> {
          taskMapper.updateEntity(dto, existing);
          return existing;
        }).map(taskMapper::toResponseDto)
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