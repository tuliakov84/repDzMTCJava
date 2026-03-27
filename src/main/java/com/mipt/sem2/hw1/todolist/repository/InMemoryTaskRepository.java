package com.mipt.sem2.hw1.todolist.repository;

import com.mipt.sem2.hw1.todolist.model.Task;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class InMemoryTaskRepository implements TaskRepository {
  private final Map<UUID, Task> storage = new ConcurrentHashMap<>();

  @Override
  public List<Task> findAll() {
    return List.copyOf(storage.values());
  }

  @Override
  public Optional<Task> findById(UUID id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Task save(Task task) {
    if (task == null) {
      throw new IllegalArgumentException("Task cannot be null");
    }

    if (task.getId() == null) {
      task.setId(UUID.randomUUID());
    }
    storage.put(task.getId(), task);
    return task;
  }

  @Override
  public void deleteById(UUID id) {
    storage.remove(id);
  }

  @Override
  public boolean existsById(UUID id) {
    return storage.containsKey(id);
  }
}
