package com.mipt.sem2.hw1.todolist.repository;

import com.mipt.sem2.hw1.todolist.model.Task;
import com.mipt.sem2.hw1.todolist.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

  List<Task> findByCompletedAndPriority(boolean completed, Priority priority);

  @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :start AND :end")
  List<Task> findTasksDueWithinNextDays(@Param("start") LocalDate start, @Param("end") LocalDate end);

  @Query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.attachments")
  List<Task> findAllWithAttachments();
}