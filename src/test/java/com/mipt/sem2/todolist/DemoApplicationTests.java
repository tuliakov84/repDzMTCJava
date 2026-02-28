package com.mipt.sem2.todolist;

import com.mipt.sem2.todolist.model.Task;
import com.mipt.sem2.todolist.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerMockitoTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @MockitoBean
  private TaskService taskService;

  private String baseUrl;
  private Task testTask;
  private UUID testTaskId;

  @BeforeEach
  void setUp() {
    baseUrl = "http:localhost:" + port + "/api/tasks";
    testTaskId = UUID.randomUUID();

    testTask = new Task();
    testTask.setId(testTaskId);
    testTask.setTitle("Test Task");
    testTask.setDescription("Test Description");
    testTask.setCompleted(false);
  }

  @Test
  void getAllTasksShouldReturnListOfTasks() {
    when(taskService.getAllTasks()).thenReturn(List.of(testTask));

    ResponseEntity<Task[]> response = restTemplate.getForEntity(baseUrl, Task[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(1);
    assertThat(response.getBody()[0].getId()).isEqualTo(testTaskId);

    verify(taskService, times(1)).getAllTasks();
  }

  @Test
  void getAllTasksWhenServiceReturnsEmptyShouldReturnEmptyList() {
    when(taskService.getAllTasks()).thenReturn(List.of());

    ResponseEntity<Task[]> response = restTemplate.getForEntity(baseUrl, Task[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEmpty();
    verify(taskService, times(1)).getAllTasks();
  }

  @Test
  void getTaskByIdWithValidIdShouldReturnTask() {
    when(taskService.getTaskById(testTaskId)).thenReturn(Optional.of(testTask));

    ResponseEntity<Task> response = restTemplate.getForEntity(
        baseUrl + "/{id}",
        Task.class,
        testTaskId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isEqualTo(testTaskId);
    verify(taskService, times(1)).getTaskById(testTaskId);
  }

  @Test
  void getTaskByIdWithInvalidIdShouldReturnNotFound() {
    UUID invalidId = UUID.randomUUID();
    when(taskService.getTaskById(invalidId)).thenReturn(Optional.empty());

    ResponseEntity<Task> response = restTemplate.getForEntity(
        baseUrl + "/{id}",
        Task.class,
        invalidId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    verify(taskService, times(1)).getTaskById(invalidId);
  }

  @Test
  void createTaskWithValidTaskShouldReturnCreatedTask() {
    Task newTask = new Task();
    newTask.setTitle("New Task");
    newTask.setDescription("New Description");

    Task savedTask = new Task();
    savedTask.setId(UUID.randomUUID());
    savedTask.setTitle("New Task");
    savedTask.setDescription("New Description");

    when(taskService.createTask(any(Task.class))).thenReturn(savedTask);

    ResponseEntity<Task> response = restTemplate.postForEntity(
        baseUrl,
        newTask,
        Task.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isNotNull();
    verify(taskService, times(1)).createTask(any(Task.class));
  }

  @Test
  void updateTaskWithValidIdShouldReturnUpdatedTask() {
    Task updatedTask = new Task();
    updatedTask.setTitle("Updated Title");

    when(taskService.updateTask(eq(testTaskId), any(Task.class)))
        .thenReturn(Optional.of(updatedTask));

    HttpEntity<Task> requestEntity = new HttpEntity<>(updatedTask);

    ResponseEntity<Task> response = restTemplate.exchange(
        baseUrl + "/{id}",
        HttpMethod.PUT,
        requestEntity,
        Task.class,
        testTaskId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    verify(taskService, times(1)).updateTask(eq(testTaskId), any(Task.class));
  }

  @Test
  void updateTaskWithInvalidIdShouldReturnNotFound() {
    UUID invalidId = UUID.randomUUID();
    Task updatedTask = new Task();
    updatedTask.setTitle("Updated Title");

    when(taskService.updateTask(eq(invalidId), any(Task.class)))
        .thenReturn(Optional.empty());

    HttpEntity<Task> requestEntity = new HttpEntity<>(updatedTask);

    ResponseEntity<Task> response = restTemplate.exchange(
        baseUrl + "/{id}",
        HttpMethod.PUT,
        requestEntity,
        Task.class,
        invalidId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    verify(taskService, times(1)).updateTask(eq(invalidId), any(Task.class));
  }

  @Test
  void deleteTaskWithValidIdShouldReturnNoContent() {
    when(taskService.deleteTask(testTaskId)).thenReturn(true);

    ResponseEntity<Void> response = restTemplate.exchange(
        baseUrl + "/{id}",
        HttpMethod.DELETE,
        null,
        Void.class,
        testTaskId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    verify(taskService, times(1)).deleteTask(testTaskId);
  }

  @Test
  void deleteTaskWithInvalidIdShouldReturnNotFound() {
    UUID invalidId = UUID.randomUUID();
    when(taskService.deleteTask(invalidId)).thenReturn(false);

    ResponseEntity<Void> response = restTemplate.exchange(
        baseUrl + "/{id}",
        HttpMethod.DELETE,
        null,
        Void.class,
        invalidId
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    verify(taskService, times(1)).deleteTask(invalidId);
  }
}