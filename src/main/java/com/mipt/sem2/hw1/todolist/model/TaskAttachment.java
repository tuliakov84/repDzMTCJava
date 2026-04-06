package com.mipt.sem2.hw1.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "task_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAttachment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id", nullable = false)
  @JsonIgnore
  private Task task;

  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private String storedFileName;

  private String contentType;

  private long size;

  private LocalDateTime uploadedAt;

  public UUID getTaskId() {
    return task != null ? task.getId() : null;
  }
}
