package com.mipt.sem2.hw1.todolist.controller;

import com.mipt.sem2.hw1.todolist.dto.AttachmentResponseDto;
import com.mipt.sem2.hw1.todolist.exception.AttachmentNotFoundException;
import com.mipt.sem2.hw1.todolist.model.TaskAttachment;
import com.mipt.sem2.hw1.todolist.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Вложения", description = "Управление вложениями задач")
public class AttachmentController {

  private final AttachmentService attachmentService;

  @PostMapping("/api/tasks/{taskId}/attachments")
  @Operation(summary = "Загрузить файл", description = "Загружает файл для задачи")
  @ApiResponse(responseCode = "201", description = "Файл загружен")
  @ApiResponse(responseCode = "400", description = "Некорректные данные")
  @ApiResponse(responseCode = "404", description = "Задача не найдена")
  public ResponseEntity<AttachmentResponseDto> uploadAttachment(
      @PathVariable UUID taskId,
      @RequestParam("file") MultipartFile file) throws IOException {
    TaskAttachment saved = attachmentService.storeAttachment(taskId, file);
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDto(saved));
  }

  @GetMapping("/api/attachments/{attachmentId}")
  @Operation(summary = "Скачать файл", description = "Скачивает файл по ID вложения")
  @ApiResponse(responseCode = "200", description = "Файл найден")
  @ApiResponse(responseCode = "404", description = "Вложение не найдено")
  public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentService.getAttachment(attachmentId)
        .orElseThrow(() -> new AttachmentNotFoundException(attachmentId));
    Resource resource = attachmentService.loadAsResource(attachmentId);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, attachment.getContentType())
        .body(resource);
  }

  @DeleteMapping("/api/attachments/{attachmentId}")
  @Operation(summary = "Удалить файл", description = "Удаляет файл по ID вложения")
  @ApiResponse(responseCode = "204", description = "Файл удалён")
  @ApiResponse(responseCode = "404", description = "Вложение не найдено")
  public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) throws IOException {
    attachmentService.deleteAttachment(attachmentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/api/tasks/{taskId}/attachments")
  @Operation(summary = "Получить список вложений задачи", description = "Возвращает метаданные всех вложений задачи")
  @ApiResponse(responseCode = "200", description = "Список получен")
  public ResponseEntity<List<AttachmentResponseDto>> getTaskAttachments(@PathVariable UUID taskId) {
    List<AttachmentResponseDto> dtos = attachmentService.getAttachmentsByTaskId(taskId).stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
  }

  private AttachmentResponseDto toResponseDto(TaskAttachment attachment) {
    AttachmentResponseDto dto = new AttachmentResponseDto();
    dto.setId(attachment.getId());
    dto.setFileName(attachment.getFileName());
    dto.setSize(attachment.getSize());
    dto.setUploadedAt(attachment.getUploadedAt());
    return dto;
  }
}
