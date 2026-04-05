package com.mipt.sem2.hw1.todolist.dto;

import com.mipt.sem2.hw1.todolist.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Schema(description = "Ответ с данными задачи")
public class TaskResponseDto {

  @Schema(description = "Идентификатор задачи", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
  private UUID id;

  @Schema(description = "Название задачи", example = "Купить молоко")
  private String title;

  @Schema(description = "Описание задачи", example = "Не забыть купить молоко в магазине")
  private String description;

  @Schema(description = "Статус выполнения", example = "false")
  private boolean completed;

  @Schema(description = "Дата и время создания", example = "2026-03-24T12:00:00")
  private LocalDateTime createdAt;

  @Schema(description = "Дата выполнения", example = "2026-12-31")
  private LocalDate dueDate;

  @Schema(description = "Приоритет", allowableValues = {"LOW", "MEDIUM", "HIGH"})
  private Priority priority;

  @Schema(description = "Теги задачи", example = "[\"работа\", \"срочно\"]")
  private Set<String> tags;
}