package com.mipt.sem2.hw1.todolist.service;

import com.mipt.sem2.hw1.todolist.exception.AttachmentNotFoundException;
import com.mipt.sem2.hw1.todolist.model.Task;
import com.mipt.sem2.hw1.todolist.model.TaskAttachment;
import com.mipt.sem2.hw1.todolist.repository.TaskAttachmentRepository;
import com.mipt.sem2.hw1.todolist.repository.TaskRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {

  private final TaskAttachmentRepository attachmentRepository;
  private final TaskRepository taskRepository;

  @Value("${app.upload.dir:uploads}")
  private String uploadDir;

  @Transactional
  public TaskAttachment storeAttachment(UUID taskId, MultipartFile file) throws IOException {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

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

    Path filePath = uploadPath.resolve(storedFileName);
    Files.copy(file.getInputStream(), filePath);

    TaskAttachment attachment = new TaskAttachment();
    attachment.setTask(task);  // раньше тут был ID теперь в Attachment Task а не ID
    attachment.setFileName(originalFileName);
    attachment.setStoredFileName(storedFileName);
    attachment.setContentType(file.getContentType());
    attachment.setSize(file.getSize());
    attachment.setUploadedAt(LocalDateTime.now());

    log.info("Storing attachment for task: {}, file: {}", taskId, originalFileName);
    return attachmentRepository.save(attachment);
  }

  public Optional<TaskAttachment> getAttachment(Long attachmentId) {
    return attachmentRepository.findById(attachmentId);
  }

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

  public List<TaskAttachment> getAttachmentsByTaskId(UUID taskId) {
    return attachmentRepository.findByTaskId(taskId);
  }
}