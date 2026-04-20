package com.mipt.sem2.hw4.external;

import com.mipt.sem2.hw4.dto.TaskDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/external/v1")
public class ExternalApiController {

    private final Map<Long, TaskDto> taskStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task) {
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        long id = idGenerator.getAndIncrement();
        task.setId(id);
        taskStore.put(id, task);

        return ResponseEntity
            .created(URI.create("/external/v1/tasks/" + id))
            .body(task);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id) {
        TaskDto task = taskStore.get(id);
        if (task == null) {
            ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
            problem.setTitle("Task not found");
            problem.setDetail("Task with id " + id + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTasks(
        @RequestParam(required = false) Boolean completed,
        @RequestParam(required = false) Integer limit) {
        var stream = taskStore.values().stream();
        if (completed != null) {
            stream = stream.filter(t -> completed.equals(t.getCompleted()));
        }
        if (limit != null && limit > 0) {
            stream = stream.limit(limit);
        }
        return ResponseEntity.ok(stream.collect(Collectors.toList()));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskStore.containsKey(id)) {
            ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
            problem.setTitle("Task not found");
            problem.setDetail("Task with id " + id + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .build();
        }
        taskStore.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unstable")
    public ResponseEntity<?> unstable(@RequestParam String mode, HttpServletRequest request) throws InterruptedException {
        log.info("Unstable endpoint called with mode={}", mode);
        switch (mode.toLowerCase()) {
            case "timeout" -> {
                Thread.sleep(5000); // больше read timeout (3 сек)
                return ResponseEntity.ok("Delayed response");
            }
            case "500" -> {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error");
            }
            case "429" -> {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Retry-After", "30");
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .headers(headers)
                    .body("Rate limit exceeded");
            }
            case "html" -> {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.TEXT_HTML)
                    .body("<html><body><h1>502 Bad Gateway</h1></body></html>");
            }
            default -> {
                return ResponseEntity.badRequest().body("Invalid mode");
            }
        }
    }
}