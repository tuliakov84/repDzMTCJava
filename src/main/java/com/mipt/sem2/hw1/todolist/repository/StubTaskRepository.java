package com.mipt.sem2.hw1.todolist.repository;

import com.mipt.sem2.hw1.todolist.model.Task;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StubTaskRepository implements TaskRepository {
  private final Map<UUID, Task> stubData = new ConcurrentHashMap<>();

  public StubTaskRepository() {
    UUID id1 = UUID.randomUUID(), id2 = UUID.randomUUID();
  }

  @Override
  public List<Task> findAll() {
    return List.copyOf(stubData.values());
  }

  @Override
  public Optional<Task> findById(UUID id) {
    return Optional.ofNullable(stubData.get(id));
  }

  @Override
  public Task save(Task task) {
    throw new UnsupportedOperationException("Stub repository is read-only");
  }

  @Override
  public void deleteById(UUID id) {
    System.out.println("deleted");
  }

  @Override
  public boolean existsById(UUID id) {
    return stubData.containsKey(id);
  }
}
