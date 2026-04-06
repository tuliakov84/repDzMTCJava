package com.mipt.sem2.hw1.todolist.service;


import com.mipt.sem2.hw1.todolist.exception.TaskNotFoundException;
import com.mipt.sem2.hw1.todolist.model.Task;
import com.mipt.sem2.hw1.todolist.repository.TaskRepository;
import com.mipt.sem2.hw1.todolist.validator.DueDateNotBeforeCreation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.*;
import java.util.function.Function;
import org.springframework.validation.annotation.Validated;

import org.springframework.beans.factory.annotation.Value;

@Service
@Validated
@Transactional(readOnly = true)
public class TaskService {

  private final TaskRepository taskRepository;

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Optional<Task> getTaskById(UUID id) {
    return taskRepository.findById(id);
  }

  @Transactional
  @DueDateNotBeforeCreation
  public Task createTask(Task task) {
    return taskRepository.save(task);
  }

  @Transactional
  @DueDateNotBeforeCreation
  public Optional<Task> updateTask(UUID id, Function<Task, Task> updater) {
    return taskRepository.findById(id)
        .map(updater)
        .map(taskRepository::save);
  }

  @Transactional
  public boolean deleteTask(UUID id) {
    if (taskRepository.existsById(id)) {
      taskRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @Transactional(rollbackFor = TaskNotFoundException.class)
  public void bulkCompleteTasks(List<UUID> ids) throws TaskNotFoundException {
    for (UUID id : ids) {
      Task task = taskRepository.findById(id)
          .orElseThrow(() -> new TaskNotFoundException("Task not found: " + id));
      task.setCompleted(true);
      taskRepository.save(task);
    }
  }

  public List<Task> getAllTasksWithAttachments() {
    return taskRepository.findAllWithAttachments();
  }
}
