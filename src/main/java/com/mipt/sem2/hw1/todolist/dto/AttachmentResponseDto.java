package com.mipt.sem2.hw1.todolist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с метаданными вложения")
public class AttachmentResponseDto {

  @Schema(description = "Идентификатор вложения", example = "42")
  private Long id;

  @Schema(description = "Оригинальное имя файла", example = "report.pdf")
  private String fileName;

  @Schema(description = "Размер файла в байтах", example = "1048576")
  private long size;

  @Schema(description = "Дата и время загрузки", example = "2026-03-24T15:30:00")
  private LocalDateTime uploadedAt;
}