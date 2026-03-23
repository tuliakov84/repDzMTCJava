package com.mipt.sem2.hw1.todolist.dto;

import lombok.Data;
import com.mipt.sem2.hw1.todolist.enums.Priority;
import java.time.LocalDate;
import java.util.Set;

@Data
public class TaskUpdateDto {
  private String title;
  private String description;
  private Boolean completed;
  private LocalDate dueDate;
  private Priority priority;
  private Set<String> tags;
}
