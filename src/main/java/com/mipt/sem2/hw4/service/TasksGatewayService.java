package com.mipt.sem2.hw4.service;

import com.mipt.sem2.hw4.client.ExternalTasksClient;
import com.mipt.sem2.hw4.dto.TaskDto;
import com.mipt.sem2.hw4.exception.ExternalApiException;
import com.mipt.sem2.hw4.exception.TaskNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TasksGatewayService {

    private final ExternalTasksClient externalTasksClient;

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "createTaskFallback")
    public TaskDto createTask(TaskDto task) {
        return externalTasksClient.createTask(task);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getTaskFallback")
    public TaskDto getTask(Long id) {
        return externalTasksClient.getTask(id)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getTasksFallback")
    public List<TaskDto> getTasks(Boolean completed, Integer limit) {
        return externalTasksClient.getTasks(completed, limit);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "deleteTaskFallback")
    public void deleteTask(Long id) {
        externalTasksClient.deleteTask(id);
    }

    private TaskDto createTaskFallback(TaskDto task, Throwable t) {
        log.warn("Fallback for createTask: {}", t.getMessage());
        return TaskDto.builder()
            .id(-1L)
            .title("Fallback Task")
            .description("Service is temporarily unavailable. Original: " + task.getTitle())
            .completed(false)
            .build();
    }

    private TaskDto getTaskFallback(Long id, Throwable t) {
        log.warn("Fallback for getTask({}): {}", id, t.getMessage());
        return TaskDto.builder()
            .id(id)
            .title("Fallback Task")
            .description("Unable to retrieve task due to: " + t.getMessage())
            .completed(false)
            .build();
    }

    private List<TaskDto> getTasksFallback(Boolean completed, Integer limit, Throwable t) {
        log.warn("Fallback for getTasks: {}", t.getMessage());
        return Collections.singletonList(
            TaskDto.builder()
                .id(-1L)
                .title("Fallback Task List")
                .description("Service degraded. Reason: " + t.getMessage())
                .completed(false)
                .build()
        );
    }

    private void deleteTaskFallback(Long id, Throwable t) {
        log.warn("Fallback for deleteTask({}): {}", id, t.getMessage());
    }
}