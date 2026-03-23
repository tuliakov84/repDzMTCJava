package com.mipt.sem2.hw1.todolist.dto;

import lombok.Data;
import com.mipt.sem2.hw1.todolist.enums.Priority;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
public class TaskResponseDto {
  private UUID id;
  private String title;
  private String description;
  private boolean completed;
  private LocalDateTime createdAt;
  private LocalDate dueDate;
  private Priority priority;
  private Set<String> tags;
}
