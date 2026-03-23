package com.mipt.sem2.hw1.todolist.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;
import com.mipt.sem2.hw1.todolist.enums.Priority;

@Data
public class TaskCreateDto {
  private String title;
  private String description;
  private LocalDate dueDate;
  private Priority priority;
  private Set<String> tags;
}
