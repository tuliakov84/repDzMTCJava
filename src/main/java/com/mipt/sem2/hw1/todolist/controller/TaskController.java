package com.mipt.sem2.hw1.todolist.controller;

import com.mipt.sem2.hw1.todolist.dto.*;
import com.mipt.sem2.hw1.todolist.mapper.TaskMapper;
import com.mipt.sem2.hw1.todolist.model.Task;
import com.mipt.sem2.hw1.todolist.service.TaskService;
import com.mipt.sem2.hw1.todolist.dto.OnCreate;
import com.mipt.sem2.hw1.todolist.dto.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "Управление задачами")
public class TaskController {

  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @GetMapping
  @Operation(summary = "Получить все задачи", description = "Возвращает список всех задач")
  @ApiResponse(responseCode = "200", description = "Успешно получен список задач")
  public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
    List<TaskResponseDto> tasks = taskService.getAllTasks().stream()
        .map(taskMapper::toResponseDto)
        .collect(Collectors.toList());
    return ResponseEntity.ok()
        .header("X-Total-Count", String.valueOf(tasks.size()))
        .body(tasks);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу с указанным идентификатором")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Задача найдена"),
      @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  public ResponseEntity<TaskResponseDto> getTaskById(
      @Parameter(description = "ID задачи", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
      @PathVariable UUID id) {
    return taskService.getTaskById(id)
        .map(taskMapper::toResponseDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Operation(summary = "Создать новую задачу", description = "Создаёт задачу на основе переданных данных")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity<TaskResponseDto> createTask(
      @Validated(OnCreate.class) @RequestBody TaskCreateDto dto) {
    Task task = taskMapper.toEntity(dto);
    Task created = taskService.createTask(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toResponseDto(created));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить задачу", description = "Обновляет существующую задачу")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Задача обновлена"),
      @ApiResponse(responseCode = "404", description = "Задача не найдена"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные")
  })
  public ResponseEntity<TaskResponseDto> updateTask(
      @Parameter(description = "ID задачи", required = true) @PathVariable UUID id,
      @Validated(OnUpdate.class) @RequestBody TaskUpdateDto dto) {
    // Используем метод сервиса, принимающий Function<Task, Task>
    return taskService.updateTask(id, existing -> {
          taskMapper.updateEntity(dto, existing);
          return existing;
        })
        .map(taskMapper::toResponseDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить задачу", description = "Удаляет задачу по ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Задача удалена"),
      @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  public ResponseEntity<Void> deleteTask(
      @Parameter(description = "ID задачи", required = true) @PathVariable UUID id) {
    if (taskService.deleteTask(id)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}