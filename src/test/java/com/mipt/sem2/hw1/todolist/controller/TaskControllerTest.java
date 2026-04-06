package com.mipt.sem2.hw1.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.sem2.hw1.todolist.dto.*;
import com.mipt.sem2.hw1.todolist.enums.Priority;
import com.mipt.sem2.hw1.todolist.mapper.TaskMapper;
import com.mipt.sem2.hw1.todolist.model.Task;
import com.mipt.sem2.hw1.todolist.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class,
    excludeAutoConfiguration = {
        HibernateJpaAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        FlywayAutoConfiguration.class
    })
class TaskControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private TaskService taskService;

  @MockitoBean
  private TaskMapper taskMapper;

  @Test
  void createTask_validDto_returnsCreated() throws Exception {
    TaskCreateDto createDto = new TaskCreateDto();
    createDto.setTitle("Test Task");
    createDto.setDescription("Description");
    createDto.setDueDate(LocalDate.now().plusDays(1));
    createDto.setPriority(Priority.MEDIUM);

    UUID taskId = UUID.randomUUID();
    Task taskEntity = new Task();
    taskEntity.setId(taskId);
    taskEntity.setTitle("Test Task");
    taskEntity.setDescription("Description");
    taskEntity.setDueDate(LocalDate.now().plusDays(1));
    taskEntity.setPriority(Priority.MEDIUM);

    TaskResponseDto responseDto = new TaskResponseDto();
    responseDto.setId(taskId);
    responseDto.setTitle("Test Task");
    responseDto.setDescription("Description");
    responseDto.setDueDate(LocalDate.now().plusDays(1));
    responseDto.setPriority(Priority.MEDIUM);

    when(taskMapper.toEntity(any(TaskCreateDto.class))).thenReturn(taskEntity);
    when(taskService.createTask(any(Task.class))).thenReturn(taskEntity);
    when(taskMapper.toResponseDto(any(Task.class))).thenReturn(responseDto);

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(taskId.toString()))
        .andExpect(jsonPath("$.title").value("Test Task"));
  }

  @Test
  void createTask_invalidTitle_returnsBadRequest() throws Exception {
    TaskCreateDto invalidDto = new TaskCreateDto();
    invalidDto.setTitle("ab");

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getTaskById_notFound_returns404() throws Exception {
    UUID id = UUID.randomUUID();
    when(taskService.getTaskById(id)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/tasks/{id}", id))
        .andExpect(status().isNotFound());
  }

  @Test
  void getTaskById_found_returnsTask() throws Exception {
    UUID id = UUID.randomUUID();
    Task taskEntity = new Task();
    taskEntity.setId(id);
    taskEntity.setTitle("Found Task");

    TaskResponseDto responseDto = new TaskResponseDto();
    responseDto.setId(id);
    responseDto.setTitle("Found Task");

    when(taskService.getTaskById(id)).thenReturn(Optional.of(taskEntity));
    when(taskMapper.toResponseDto(taskEntity)).thenReturn(responseDto);

    mockMvc.perform(get("/api/tasks/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.title").value("Found Task"));
  }

  @Test
  void updateTask_validDto_returnsUpdated() throws Exception {
    UUID id = UUID.randomUUID();
    TaskUpdateDto updateDto = new TaskUpdateDto();
    updateDto.setTitle("Updated Title");

    Task existing = new Task();
    existing.setId(id);
    existing.setTitle("Old Title");

    Task updated = new Task();
    updated.setId(id);
    updated.setTitle("Updated Title");

    TaskResponseDto responseDto = new TaskResponseDto();
    responseDto.setId(id);
    responseDto.setTitle("Updated Title");

    when(taskService.updateTask(any(UUID.class), any(Function.class)))
        .thenAnswer(invocation -> {
          Function<Task, Task> updater = invocation.getArgument(1);
          Task result = updater.apply(existing);
          return Optional.of(result);
        });
    when(taskMapper.toResponseDto(any(Task.class))).thenReturn(responseDto);

    mockMvc.perform(put("/api/tasks/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Title"));
  }

  @Test
  void deleteTask_existing_returnsNoContent() throws Exception {
    UUID id = UUID.randomUUID();
    when(taskService.deleteTask(id)).thenReturn(true);

    mockMvc.perform(delete("/api/tasks/{id}", id))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteTask_notFound_returnsNotFound() throws Exception {
    UUID id = UUID.randomUUID();
    when(taskService.deleteTask(id)).thenReturn(false);

    mockMvc.perform(delete("/api/tasks/{id}", id))
        .andExpect(status().isNotFound());
  }

  @Test
  void getAllTasks_returnsListWithTotalCountHeader() throws Exception {
    UUID id = UUID.randomUUID();
    Task task = new Task();
    task.setId(id);
    task.setTitle("Task 1");

    TaskResponseDto response = new TaskResponseDto();
    response.setId(id);
    response.setTitle("Task 1");

    when(taskService.getAllTasks()).thenReturn(List.of(task));
    when(taskMapper.toResponseDto(task)).thenReturn(response);

    mockMvc.perform(get("/api/tasks"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-Total-Count", "1"))
        .andExpect(jsonPath("$[0].id").value(id.toString()))
        .andExpect(jsonPath("$[0].title").value("Task 1"));
  }
}
