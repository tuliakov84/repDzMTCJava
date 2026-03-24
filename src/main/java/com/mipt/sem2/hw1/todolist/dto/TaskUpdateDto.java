package com.mipt.sem2.hw1.todolist.dto;

import com.mipt.sem2.hw1.todolist.enums.Priority;
import com.mipt.sem2.hw1.todolist.dto.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Schema(description = "DTO для обновления задачи")
public class TaskUpdateDto {

  @Size(min = 3, max = 100, groups = OnUpdate.class)
  @Schema(description = "Название задачи", example = "Купить молоко", minLength = 3, maxLength = 100)
  private String title;

  @Size(max = 500, groups = OnUpdate.class)
  @Schema(description = "Описание задачи", example = "Не забыть купить молоко в магазине", maxLength = 500)
  private String description;

  @Schema(description = "Статус выполнения задачи", example = "true")
  private Boolean completed;

  @Schema(description = "Дата выполнения", example = "2026-12-31")
  private LocalDate dueDate;

  @Schema(description = "Приоритет задачи", allowableValues = {"LOW", "MEDIUM", "HIGH"})
  private Priority priority;

  @Size(max = 5, groups = OnUpdate.class)
  @Schema(description = "Теги задачи (не более 5)", example = "[\"работа\", \"срочно\"]", maxLength = 5)
  private Set<String> tags;
}