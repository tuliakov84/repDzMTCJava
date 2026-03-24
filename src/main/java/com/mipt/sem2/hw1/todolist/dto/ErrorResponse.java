package com.mipt.sem2.hw1.todolist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@Schema(description = "Структура ответа при ошибке")
public class ErrorResponse {

  @Schema(description = "Время ошибки", example = "2026-03-24T15:30:00Z")
  private Instant timestamp;

  @Schema(description = "HTTP статус код", example = "400")
  private int status;

  @Schema(description = "Краткое описание ошибки", example = "Bad Request")
  private String error;

  @Schema(description = "Детальное сообщение для клиента", example = "Validation failed")
  private String message;

  @Schema(description = "Путь запроса", example = "/api/tasks")
  private String path;

  @Schema(description = "Дополнительные детали ошибки", example = "{\"title\": \"размер должен быть между 3 и 100\"}")
  private Map<String, Object> details;
}