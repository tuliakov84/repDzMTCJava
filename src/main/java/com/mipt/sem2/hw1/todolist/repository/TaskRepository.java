package com.mipt.sem2.hw1.todolist.repository;

import com.mipt.sem2.hw1.todolist.model.Task;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
  List<Task> findAll();
  Optional<Task> findById(UUID id);
  Task save(Task task);
  void deleteById(UUID id);
  boolean existsById(UUID id);
}