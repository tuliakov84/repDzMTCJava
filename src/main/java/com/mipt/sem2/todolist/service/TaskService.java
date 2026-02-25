package com.mipt.sem2.todolist.service;

import com.mipt.sem2.todolist.model.Task;
import com.mipt.sem2.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
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
}