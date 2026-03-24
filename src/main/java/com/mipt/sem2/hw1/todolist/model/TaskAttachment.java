package com.mipt.sem2.hw1.todolist.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAttachment {
  private Long id;
  private UUID taskId;
  private String fileName;
  private String storedFileName;
  private String contentType;
  private long size;
  private LocalDateTime uploadedAt;
}
