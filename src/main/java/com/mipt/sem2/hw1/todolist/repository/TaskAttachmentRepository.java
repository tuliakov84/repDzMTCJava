package com.mipt.sem2.hw1.todolist.repository;

import com.mipt.sem2.hw1.todolist.model.TaskAttachment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskAttachmentRepository {
  TaskAttachment save(TaskAttachment attachment);

  Optional<TaskAttachment> findById(Long id);

  List<TaskAttachment> findByTaskId(UUID taskId);

  void deleteById(Long id);

  boolean existsById(Long id);
}
