package com.mipt.sem2.hw1.todolist.service;

import com.mipt.sem2.hw1.todolist.model.TaskAttachment;
import com.mipt.sem2.hw1.todolist.repository.TaskAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
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
public class AttachmentService {

  private final TaskAttachmentRepository attachmentRepository;

  @Value("${app.upload.dir:uploads}")
  private String uploadDir;

  public TaskAttachment storeAttachment(UUID taskId, MultipartFile file) throws IOException {
    String storedFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }
    Path filePath = uploadPath.resolve(storedFileName);
    Files.copy(file.getInputStream(), filePath);

    TaskAttachment attachment = new TaskAttachment();
    attachment.setTaskId(taskId);
    attachment.setFileName(file.getOriginalFilename());
    attachment.setStoredFileName(storedFileName);
    attachment.setContentType(file.getContentType());
    attachment.setSize(file.getSize());
    attachment.setUploadedAt(LocalDateTime.now());

    return attachmentRepository.save(attachment);
  }

  public Optional<TaskAttachment> getAttachment(Long attachmentId) {
    return attachmentRepository.findById(attachmentId);
  }

  public Resource loadAsResource(Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new RuntimeException("Attachment not found"));
    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Resource resource = new UrlResource(filePath.toUri());
    if (resource.exists() && resource.isReadable()) {
      return resource;
    } else {
      throw new RuntimeException("File not found on disk");
    }
  }

  public void deleteAttachment(Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new RuntimeException("Attachment not found"));
    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Files.deleteIfExists(filePath);
    attachmentRepository.deleteById(attachmentId);
  }

  public List<TaskAttachment> getAttachmentsByTaskId(UUID taskId) {
    return attachmentRepository.findByTaskId(taskId);
  }
}