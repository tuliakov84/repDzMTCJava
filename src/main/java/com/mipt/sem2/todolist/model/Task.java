package com.mipt.sem2.todolist.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
  private UUID id;
  private String title;
  private String description;
  private boolean completed;
}