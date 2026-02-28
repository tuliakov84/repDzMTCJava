package com.mipt.sem2.todolist.service;

import com.mipt.sem2.todolist.model.Task;
import com.mipt.sem2.todolist.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {
  private final TaskRepository taskRepository;
  private Map<String, Task> taskCache = new HashMap<>();

  @Value("${app.name}")
  private String appName;

  @Value("${app.version}")
  private String appVersion;

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

  public Task createTask(Task task) {
    return taskRepository.save(task);
  }

  public Optional<Task> updateTask(UUID id, Task updatedTask) {
    return taskRepository.findById(id)
        .map(existing -> {
          existing.setTitle(updatedTask.getTitle());
          existing.setDescription(updatedTask.getDescription());
          existing.setCompleted(updatedTask.isCompleted());
          return taskRepository.save(existing);
        });
  }

  public boolean deleteTask(UUID id) {
    if (taskRepository.existsById(id)) {
      taskRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @PostConstruct
  public void initCache() {
    System.out.println("TaskService: initializing cache with tasks from repository");
    taskRepository.findAll().forEach(task -> taskCache.put(task.getTitle(), task));
    System.out.println("TaskService: cache initialized with " + taskCache.size() + " tasks");
  }

  @PreDestroy
  public void destroyCache() {
    System.out.println("TaskService: cleaning up cache. Tasks in cache: " + taskCache.size());
    taskCache.clear();
  }
}