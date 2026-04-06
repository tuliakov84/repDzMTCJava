package com.mipt.sem2.hw1.todolist.service;

import com.mipt.sem2.hw1.todolist.exception.TaskNotFoundException;
import com.mipt.sem2.hw1.todolist.model.Task;
import com.mipt.sem2.hw1.todolist.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceIntegrationTest {

  @Autowired
  private TaskService taskService;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  void bulkCompleteTasks_shouldRollbackOnInvalidId() {
    Task task1 = new Task();
    task1.setTitle("Task 1");
    task1.setCompleted(false);
    Task task2 = new Task();
    task2.setTitle("Task 2");
    task2.setCompleted(false);
    Task saved1 = taskService.createTask(task1);
    Task saved2 = taskService.createTask(task2);
    UUID invalidId = UUID.randomUUID();

    assertThatThrownBy(() -> taskService.bulkCompleteTasks(List.of(saved1.getId(), invalidId, saved2.getId())))
        .isInstanceOf(TaskNotFoundException.class);

    Task refreshed1 = taskRepository.findById(saved1.getId()).orElseThrow();
    Task refreshed2 = taskRepository.findById(saved2.getId()).orElseThrow();
    assertThat(refreshed1.isCompleted()).isFalse();
    assertThat(refreshed2.isCompleted()).isFalse();
  }
}