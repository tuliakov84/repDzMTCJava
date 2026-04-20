package com.mipt.sem2.hw4.client;

import com.mipt.sem2.hw4.dto.TaskDto;
import com.mipt.sem2.hw4.exception.ExternalApiException;
import com.mipt.sem2.hw4.exception.TaskNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalTasksClient {

    private final RestClient externalTasksRestClient;
    private final ObjectMapper objectMapper;

    public TaskDto createTask(TaskDto task) {
        ResponseEntity<TaskDto> response = externalTasksRestClient.post()
            .uri("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .body(task)
            .retrieve()
            .onStatus(status -> status.value() == 500, (req, res) -> {
                throw new ExternalApiException("External service error");
            })
            .onStatus(status -> status.value() == 429, (req, res) -> {
                throw new ExternalApiException("Rate limited by external service");
            })
            .toEntity(TaskDto.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            URI location = response.getHeaders().getLocation();
            if (location != null) {
                String path = location.getPath();
                String id = path.substring(path.lastIndexOf('/') + 1);
                task.setId(Long.parseLong(id));
            }
            return response.getBody();
        } else {
            throw new ExternalApiException("Unexpected response status: " + response.getStatusCode());
        }
    }

    public Optional<TaskDto> getTask(Long id) {
        try {
            ResponseEntity<TaskDto> response = externalTasksRestClient.get()
                .uri("/tasks/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND, (req, res) -> {
                    ProblemDetail problemDetail;
                    try {
                        problemDetail = objectMapper.readValue(res.getBody(), ProblemDetail.class);
                    } catch (IOException e) {
                        throw new TaskNotFoundException("Task not found");
                    }
                    String detail = problemDetail != null ? problemDetail.getDetail() : "Task not found";
                    throw new TaskNotFoundException(detail);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    MediaType contentType = res.getHeaders().getContentType();
                    if (contentType == null || !contentType.includes(MediaType.APPLICATION_JSON)) {
                        try {
                            String body = new String(res.getBody().readAllBytes());
                            log.warn("Received non-JSON response from external API. Content-Type: {}, Body preview: {}",
                                contentType, body.substring(0, Math.min(200, body.length())));
                        } catch (IOException e) {
                            log.warn("Could not read response body for non-JSON error", e);
                        }
                    }
                    throw new ExternalApiException("External API server error");
                })
                .toEntity(TaskDto.class);

            return Optional.ofNullable(response.getBody());
        } catch (TaskNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException("Failed to fetch task", e);
        }
    }

    public List<TaskDto> getTasks(Boolean completed, Integer limit) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/tasks");
        if (completed != null) {
            builder.queryParam("completed", completed);
        }
        if (limit != null) {
            builder.queryParam("limit", limit);
        }

        try {
            return externalTasksRestClient.get()
                .uri(builder.build().toUriString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            throw new ExternalApiException("Failed to fetch tasks", e);
        }
    }

    public void deleteTask(Long id) {
        try {
            ResponseEntity<Void> response = externalTasksRestClient.delete()
                .uri("/tasks/{id}", id)
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND, (req, res) -> {
                    ProblemDetail problemDetail;
                    try {
                        problemDetail = objectMapper.readValue(res.getBody(), ProblemDetail.class);
                    } catch (IOException e) {
                        throw new TaskNotFoundException("Task not found");
                    }
                    String detail = problemDetail != null ? problemDetail.getDetail() : "Task not found";
                    throw new TaskNotFoundException(detail);
                })
                .toBodilessEntity();

            if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
                throw new ExternalApiException("Unexpected status when deleting: " + response.getStatusCode());
            }
        } catch (TaskNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException("Failed to delete task", e);
        }
    }
}