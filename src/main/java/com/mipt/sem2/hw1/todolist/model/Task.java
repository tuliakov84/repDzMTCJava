package com.mipt.sem2.hw1.todolist.model;

import com.mipt.sem2.hw1.todolist.enums.Priority;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
  private UUID id;
  private String title;
  private String description;
  private boolean completed;
  private LocalDateTime createdAt;
  private LocalDate dueDate;
  private Priority priority;
  private Set<String> tags;
}
