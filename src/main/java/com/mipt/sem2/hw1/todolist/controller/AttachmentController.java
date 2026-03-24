package com.mipt.sem2.hw1.todolist.controller;

import com.mipt.sem2.hw1.todolist.dto.AttachmentResponseDto;
import com.mipt.sem2.hw1.todolist.model.TaskAttachment;
import com.mipt.sem2.hw1.todolist.service.AttachmentService;
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
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/api/tasks/{taskId}/attachments")
    public ResponseEntity<AttachmentResponseDto> uploadAttachment(
            @PathVariable UUID taskId,
            @RequestParam("file") MultipartFile file) throws IOException {
        TaskAttachment saved = attachmentService.storeAttachment(taskId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDto(saved));
    }

    @GetMapping("/api/attachments/{attachmentId}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) throws IOException {
        Resource resource = attachmentService.loadAsResource(attachmentId);
        TaskAttachment attachment = attachmentService.getAttachment(attachmentId).orElseThrow();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/api/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) throws IOException {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/tasks/{taskId}/attachments")
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