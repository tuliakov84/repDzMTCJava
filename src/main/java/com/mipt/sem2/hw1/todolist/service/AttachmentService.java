package com.mipt.sem2.hw1.todolist.service;

import com.mipt.sem2.hw1.todolist.exception.AttachmentNotFoundException;
import com.mipt.sem2.hw1.todolist.model.Task;
import com.mipt.sem2.hw1.todolist.model.TaskAttachment;
import com.mipt.sem2.hw1.todolist.repository.TaskAttachmentRepository;
import com.mipt.sem2.hw1.todolist.repository.TaskRepository;  // ← Добавили напрямую
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {

  private final TaskAttachmentRepository attachmentRepository;
  private final TaskRepository taskRepository;

  @Value("${app.upload.dir:uploads}")
  private String uploadDir;

  @Transactional
  public TaskAttachment storeAttachment(UUID taskId, MultipartFile file) throws IOException {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

    if (file.isEmpty()) {
      throw new IllegalArgumentException("Cannot store empty file");
    }

    String originalFileName = file.getOriginalFilename();
    String safeFileName = originalFileName != null
        ? originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_")
        : "unnamed";
    String storedFileName = UUID.randomUUID() + "_" + safeFileName;

    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }
    Files.copy(file.getInputStream(), uploadPath.resolve(storedFileName));

    TaskAttachment attachment = new TaskAttachment();
    attachment.setTask(task);
    attachment.setFileName(originalFileName);
    attachment.setStoredFileName(storedFileName);
    attachment.setContentType(file.getContentType());
    attachment.setSize(file.getSize());
    attachment.setUploadedAt(LocalDateTime.now());

    TaskAttachment saved = attachmentRepository.save(attachment);
    log.info("Stored attachment {} for task {}", saved.getId(), taskId);
    return saved;
  }

  @Transactional(readOnly = true)
  public Optional<TaskAttachment> getAttachment(Long attachmentId) {
    return attachmentRepository.findById(attachmentId);
  }

  @Transactional(readOnly = true)
  public Resource loadAsResource(Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new AttachmentNotFoundException(attachmentId));
    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Resource resource = new UrlResource(filePath.toUri());
    if (resource.exists() && resource.isReadable()) {
      return resource;
    } else {
      throw new RuntimeException("File not found on disk: " + attachment.getStoredFileName());
    }
  }

  @Transactional
  public void deleteAttachment(Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new AttachmentNotFoundException(attachmentId));
    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Files.deleteIfExists(filePath);
    attachmentRepository.deleteById(attachmentId);
    log.info("Deleted attachment: {} for task: {}", attachmentId, attachment.getTask().getId());
  }

  @Transactional(readOnly = true)
  public List<TaskAttachment> getAttachmentsByTaskId(UUID taskId) {
    return attachmentRepository.findByTask_Id(taskId);
  }
}