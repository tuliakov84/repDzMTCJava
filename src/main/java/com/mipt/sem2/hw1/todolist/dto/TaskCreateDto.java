package com.mipt.sem2.hw1.todolist.dto;

import com.mipt.sem2.hw1.todolist.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Schema(description = "DTO для создания новой задачи")
public class TaskCreateDto {

  @NotBlank(groups = OnCreate.class)
  @Size(min = 3, max = 100, groups = OnCreate.class)
  @Schema(description = "Название задачи", example = "Купить молоко", minLength = 3, maxLength = 100)
  private String title;

  @Size(max = 500, groups = OnCreate.class)
  @Schema(description = "Описание задачи", example = "Не забыть купить молоко в магазине", maxLength = 500)
  private String description;

  @FutureOrPresent(groups = OnCreate.class)
  @Schema(description = "Дата выполнения (не может быть в прошлом)", example = "2026-12-31")
  private LocalDate dueDate;

  @NotNull(groups = OnCreate.class)
  @Schema(description = "Приоритет задачи", allowableValues = {"LOW", "MEDIUM", "HIGH"})
  private Priority priority;

  @Size(max = 5, groups = OnCreate.class)
  @Schema(description = "Теги задачи (не более 5)", example = "[\"работа\", \"срочно\"]", maxLength = 5)
  private Set<String> tags;
}